package cinebox.domain.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.PlatformType;
import cinebox.common.exception.user.DuplicatedEmailException;
import cinebox.common.exception.user.DuplicatedPhoneException;
import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.common.utils.CookieUtil;
import cinebox.common.utils.SecurityUtil;
import cinebox.domain.auth.repository.TokenRedisRepository;
import cinebox.domain.user.dto.UserResponse;
import cinebox.domain.user.dto.UserUpdateRequest;
import cinebox.domain.user.entity.User;
import cinebox.domain.user.repository.UserRepository;
import cinebox.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final TokenRedisRepository tokenRedisRepository;

	// 전체 활성 사용자 조회
	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getAllActiveUser() {
		log.info("전체 활성 사용자 조회 서비스 시작");
		List<UserResponse> responses = userRepository.findAll().stream()
				.map(UserResponse::from)
				.collect(Collectors.toList());

		log.info("전체 활성 사용자 조회 서비스 완료, 결과 수: {}", responses.size());
		return responses;
	}

	// 특정 사용자 조회
	@Override
	@Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
		log.info("특정 사용자 조회 서비스 시작: userId={}", userId);
    	User user = userRepository.findByIdIncludingDeleted(userId)
    			.orElseThrow(() -> NotFoundUserException.EXCEPTION);
    	log.info("특정 사용자 조회 서비스 완료: userId={}", userId);
        return UserResponse.from(user);
    }

	// 본인 사용자 조회
	@Override
	@Transactional(readOnly = true)
	public UserResponse getMyInform() {
		log.info("본인 사용자 정보 조회 서비스 시작");
		User currentUser = SecurityUtil.getCurrentUser();
		log.info("본인 사용자 정보 조회 서비스 완료: userId={}", currentUser.getUserId());
		return UserResponse.from(currentUser);
	}

	// 사용자 정보 수정
	@Override
	@Transactional
	public UserResponse updateUser(Long userId, UserUpdateRequest request) {
		log.info("사용자 정보 수정 서비스 시작: userId={}", userId);
		User reqUser = userRepository.findById(userId)
				.orElseThrow(() -> {
					log.error("사용자 정보 수정 실패: userId={} 조회 결과 없음", userId);
					return NotFoundUserException.EXCEPTION;
				});
		User currentUser = SecurityUtil.getCurrentUser();
		
		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(reqUser.getUserId())) {
			log.error("사용자 정보 수정 권한 없음: currentUserId={}, targetUserId={}", currentUser.getUserId(), reqUser.getUserId());
			throw NoAuthorizedUserException.EXCEPTION;
		}
		
		String encodedPassword = null;
		if (request.password() != null && !request.password().isBlank()) {
			encodedPassword = passwordEncoder.encode(request.password());
		}
		
		validateUniqueFields(request, userId, currentUser.getPlatformType());
		
		reqUser.updateUser(request, encodedPassword);
		if (SecurityUtil.isAdmin()) {
			reqUser.updateUserRole(request.role());
		}
		
		User updatedUser = userRepository.save(reqUser);
		log.info("사용자 정보 수정 완료: userId={}", updatedUser.getUserId());
		return UserResponse.from(updatedUser);
	}

	// 회원 탈퇴
	@Override
	@Transactional
	public void withdrawUser(Long userId, HttpServletRequest request, HttpServletResponse response) {
		log.info("회원 탈퇴 서비스 시작: userId={}", userId);
		User reqUser = userRepository.findById(userId)
				.orElseThrow(() -> {
					log.error("회원 탈퇴 실패: userId={} 조회 결과 없음", userId);
					return NotFoundUserException.EXCEPTION;
				});
		User currentUser = SecurityUtil.getCurrentUser();

		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(reqUser.getUserId())) {
			log.error("회원 탈퇴 권한 없음: currentUserId={}, targetUserId={}", currentUser.getUserId(), reqUser.getUserId());
			throw NoAuthorizedUserException.EXCEPTION;
		}

		userRepository.delete(reqUser);
		log.info("회원 탈퇴 DB 처리 완료: userId={}", userId);

		Optional<Cookie> accessTokenCookie = CookieUtil.getCookie(request, "AT");
		if (accessTokenCookie.isPresent()) {
			String accessToken = accessTokenCookie.get().getValue();
			jwtTokenProvider.addAccessTokenToBlacklist(accessToken);
			log.info("JWT 블랙리스트 등록 완료: userId={}", userId);
		}

		CookieUtil.clearAuthCookies(response);
		tokenRedisRepository.deleteById(String.valueOf(reqUser.getUserId()));
		log.info("회원 탈퇴 쿠키 및 레디스 정리 완료: userId={}", userId);
	}

	// 사용자 복구
	@Override
	@Transactional
	public UserResponse restoreUser(Long userId) {
		log.info("사용자 복구 서비스 시작: userId={}", userId);
		User user = userRepository.findDeletedByUserId(userId)
    			.orElseThrow(() -> {
    				log.error("사용자 복구 실패: userId={} 조회 결과 없음", userId);
    				return NotFoundUserException.EXCEPTION;
    			});

		user.restoreUser();
		User saved = userRepository.save(user);

		log.info("사용자 복구 완료: userId={}", saved.getUserId());
		return UserResponse.from(saved);
	}
	
	private void validateUniqueFields(UserUpdateRequest request, Long userId, PlatformType platformType) {
		if (userRepository.existsByEmailAndUserIdNotAndPlatformType(request.email(), userId, platformType)) {
			log.error("중복 email 감지: {}", request.email());
			throw DuplicatedEmailException.EXCEPTION;
		}
		if (userRepository.existsByPhoneAndUserIdAndPlatformType(request.phone(), userId, platformType)) {
			log.error("중복 phone 감지: {}", request.phone());
			throw DuplicatedPhoneException.EXCEPTION;
		}
	}
}
