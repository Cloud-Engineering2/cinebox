package cinebox.domain.auth.service;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.Role;
import cinebox.common.exception.user.DuplicatedEmailException;
import cinebox.common.exception.user.DuplicatedFieldException;
import cinebox.common.exception.user.DuplicatedIdentifierException;
import cinebox.common.exception.user.DuplicatedPhoneException;
import cinebox.common.utils.CookieUtil;
import cinebox.common.utils.SecurityUtil;
import cinebox.domain.auth.dto.AuthRequest;
import cinebox.domain.auth.dto.AuthResponse;
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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	private final TokenRedisRepository tokenRedisRepository;

	@Override
	@Transactional
	public UserResponse signup(SignUpRequest request) {
		validateUniqueFields(request);

        String encodedPassword = passwordEncoder.encode(request.password());
		User newUser = User.createUser(request, encodedPassword);

		// ADMIN이 생성하는 계정이 아니라면 USER로 역할 고정
		if (!SecurityUtil.isAdmin()) {
			newUser.updateUserRole(Role.USER);
		}

		try {
			User savedUser = userRepository.save(newUser);
			return UserResponse.from(savedUser);
		} catch(DataIntegrityViolationException e) {
			throw DuplicatedFieldException.EXCEPTION;
		}
	}

	@Override
	@Transactional
	public AuthResponse login(AuthRequest request, HttpServletResponse response) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		User user = principal.getUser();
		
		String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole().name());
		String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getRole().name());
		
		TokenRedis tokenRedis = new TokenRedis(String.valueOf(user.getUserId()), accessToken, refreshToken);
		tokenRedisRepository.save(tokenRedis);

		jwtTokenProvider.saveAccessCookie(response, accessToken);
		jwtTokenProvider.saveRefreshCookie(response, refreshToken);
		return new AuthResponse(user.getUserId(), user.getRole().toString(), user.getIdentifier());
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		// 블랙리스트에 액세스 토큰 등록 
		Optional<Cookie> accessTokenCookie = CookieUtil.getCookie(request, "AT");
		if (accessTokenCookie.isPresent()) {
			String accessToken = accessTokenCookie.get().getValue();
			jwtTokenProvider.addAccessTokenToBlacklist(accessToken);
		}
		
		// 클라이언트 쿠키 삭제
		CookieUtil.clearAuthCookies(response);
	}

	private void validateUniqueFields(SignUpRequest request) {
		if (userRepository.existsByIdentifier(request.identifier())) {
			throw DuplicatedIdentifierException.EXCEPTION;
		}
		if (userRepository.existsByEmail(request.email())) {
			throw DuplicatedEmailException.EXCEPTION;
		}
		if (userRepository.existsByPhone(request.phone())) {
			throw DuplicatedPhoneException.EXCEPTION;
		}
	}
}
