package cinebox.domain.auth.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.PlatformType;
import cinebox.common.enums.Role;
import cinebox.common.exception.user.DuplicatedEmailException;
import cinebox.common.exception.user.DuplicatedFieldException;
import cinebox.common.exception.user.DuplicatedIdentifierException;
import cinebox.common.exception.user.DuplicatedPhoneException;
import cinebox.common.utils.CookieUtil;
import cinebox.common.utils.KakaoUtil;
import cinebox.common.utils.SecurityUtil;
import cinebox.domain.auth.dto.AuthRequest;
import cinebox.domain.auth.dto.AuthResponse;
import cinebox.domain.auth.dto.KakaoProfile;
import cinebox.domain.auth.dto.OAuthToken;
import cinebox.domain.auth.dto.SignUpRequest;
import cinebox.domain.auth.entity.TokenRedis;
import cinebox.domain.auth.repository.TokenRedisRepository;
import cinebox.domain.user.dto.UserResponse;
import cinebox.domain.user.entity.User;
import cinebox.domain.user.repository.UserRepository;
import cinebox.security.jwt.JwtTokenProvider;
import cinebox.security.service.PrincipalDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	private final TokenRedisRepository tokenRedisRepository;
	private final KakaoUtil kakaoUtil;

	// 회원가입
	@Override
	@Transactional
	public UserResponse signup(SignUpRequest request) {
		log.info("회원가입 프로세스 시작: identifier={}", request.identifier());
		validateUniqueFields(request);

		String encodedPassword = passwordEncoder.encode(request.password());
		User newUser = User.createUser(request, encodedPassword, PlatformType.LOCAL);

		// ADMIN이 생성하는 계정이 아니라면 USER로 역할 고정
		if (!SecurityUtil.isAdmin()) {
			newUser.updateUserRole(Role.USER);
		}

		try {
			User savedUser = userRepository.save(newUser);
			return UserResponse.from(savedUser);
		} catch(DataIntegrityViolationException e) {
			log.error("회원가입 실패 - 중복 필드 존재: {}", e.getMessage());
			throw DuplicatedFieldException.EXCEPTION;
		}
	}

	// 카카오 회원가입
	@Override
	@Transactional
	public UserResponse kakaoSignup(SignUpRequest request) {
		log.info("카카오 회원가입 프로세스 시작: identifier={}", request.identifier());
		validateUniqueFields(request);
		
		String encodedPassword = passwordEncoder.encode(java.util.UUID.randomUUID().toString());
		User newUser = User.createUser(request, encodedPassword, PlatformType.KAKAO);
		
		if (!SecurityUtil.isAdmin()) {
			newUser.updateUserRole(Role.USER);
		}

		try {
			User savedUser = userRepository.save(newUser);
			return UserResponse.from(savedUser);
		} catch(DataIntegrityViolationException e) {
			log.error("카카오 회원가입 실패 - 중복 필드 존재: {}", e.getMessage());
			throw DuplicatedFieldException.EXCEPTION;
		}
	}

	@Override
	@Transactional
	public AuthResponse login(AuthRequest request, HttpServletResponse response) {
		log.info("로그인 프로세스 시작: identifier={}", request.identifier());
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.identifier(), request.password());
		Authentication authentication = authenticationManager.authenticate(token);
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		User user = principal.getUser();
		
		String accessToken = jwtTokenProvider.createAccessToken(
				user.getUserId(),
				user.getRole().name(),
				user.getPlatformType(),
				user.getIdentifier()
		);
		String refreshToken = jwtTokenProvider.createRefreshToken(
				user.getUserId(),
				user.getRole().name(),
				user.getPlatformType(),
				user.getIdentifier()
		);
		
		TokenRedis tokenRedis = new TokenRedis(String.valueOf(user.getUserId()), accessToken, refreshToken);
		tokenRedisRepository.save(tokenRedis);

		jwtTokenProvider.saveAccessCookie(response, accessToken);
		jwtTokenProvider.saveRefreshCookie(response, refreshToken);
		log.info("로그인 성공: userId={}, accessToken 생성됨", user.getUserId());
		return new AuthResponse(user.getUserId(), user.getRole().toString(), user.getIdentifier());
	}

	@Override
	@Transactional
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		log.info("로그아웃 프로세스 시작");
		// 블랙리스트에 액세스 토큰 등록 
		Optional<Cookie> accessTokenCookie = CookieUtil.getCookie(request, "AT");
		if (accessTokenCookie.isPresent()) {
			String accessToken = accessTokenCookie.get().getValue();
			
			Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
			tokenRedisRepository.findById(String.valueOf(userId)).ifPresent(token -> {
				tokenRedisRepository.delete(token);
				log.info("Redis에서 토큰 삭제: userId={}", userId);
			});

			jwtTokenProvider.addAccessTokenToBlacklist(accessToken);
			log.info("액세스 토큰을 블랙리스트에 등록: userId={}", userId);
		}
		
		// 클라이언트 쿠키 삭제
		CookieUtil.clearAuthCookies(request, response);
		log.info("클라이언트 쿠키 삭제 완료");
	}
	
	@Override
	@Transactional
	public Object oAuthLogin(String accessCode, HttpServletResponse response) {
		log.info("OAuth 로그인 프로세스 시작: accessCode={}", accessCode);
		OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
		KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);
		String email = kakaoProfile.kakao_account().email();
		
		Optional<User> optionalUser = userRepository.findByEmailAndPlatformType(email, PlatformType.KAKAO);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			log.info("카카오 계정으로 로그인: userId={}", user.getUserId());
			
			String accessToken = jwtTokenProvider.createAccessToken(
					user.getUserId(),
					user.getRole().name(),
					user.getPlatformType(),
					user.getIdentifier()
			);
			String refreshToken = jwtTokenProvider.createRefreshToken(
					user.getUserId(),
					user.getRole().name(),
					user.getPlatformType(),
					user.getIdentifier()
			);
			
			TokenRedis tokenRedis = new TokenRedis(String.valueOf(user.getUserId()), accessToken, refreshToken);
			tokenRedisRepository.save(tokenRedis);

			jwtTokenProvider.saveAccessCookie(response, accessToken);
			jwtTokenProvider.saveRefreshCookie(response, refreshToken);
			log.info("카카오 OAuth 로그인 성공: userId={}", user.getUserId());
			return new AuthResponse(user.getUserId(), user.getRole().toString(), user.getIdentifier());
		} else {
			log.warn("카카오 OAuth 로그인: 신규 사용자, 프로필 반환");
			return kakaoProfile;
		}
	}

	private void validateUniqueFields(SignUpRequest request) {
		if (userRepository.existsByIdentifierAndPlatformType(request.identifier(), PlatformType.LOCAL)) {
			log.error("중복 identifier 감지: {}", request.identifier());
			throw DuplicatedIdentifierException.EXCEPTION;
		}
		if (userRepository.existsByEmailAndPlatformType(request.email(), PlatformType.LOCAL)) {
			log.error("중복 email 감지: {}", request.email());
			throw DuplicatedEmailException.EXCEPTION;
		}
		if (userRepository.existsByPhoneAndPlatformType(request.phone(), PlatformType.LOCAL)) {
			log.error("중복 phone 감지: {}", request.phone());
			throw DuplicatedPhoneException.EXCEPTION;
		}
	}
}
