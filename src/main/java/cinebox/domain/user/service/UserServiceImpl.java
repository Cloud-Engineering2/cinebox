package cinebox.domain.user.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		return userRepository.findAll().stream()
				.map(UserResponse::from)
				.collect(Collectors.toList());
	}

	// 특정 사용자 조회
	@Override
	@Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
    	User user = userRepository.findByIdIncludingDeleted(userId)
    			.orElseThrow(() -> NotFoundUserException.EXCEPTION);
        return UserResponse.from(user);
    }

	// 본인 사용자 조회
	@Override
	@Transactional(readOnly = true)
	public UserResponse getMyInform() {
		User currentUser = SecurityUtil.getCurrentUser();
		return UserResponse.from(currentUser);
	}

	// 사용자 정보 수정
	@Override
	@Transactional
	public UserResponse updateUser(Long userId, UserUpdateRequest request) {
		User reqUser = userRepository.findById(userId)
				.orElseThrow(() -> NotFoundUserException.EXCEPTION);
		User currentUser = SecurityUtil.getCurrentUser();
		
		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(reqUser.getUserId())) {
			throw NoAuthorizedUserException.EXCEPTION;
		}
		
		String encodedPassword = null;
		if (request.password() != null && !request.password().isBlank()) {
			encodedPassword = passwordEncoder.encode(request.password());
		}
		
		reqUser.updateUser(request, encodedPassword);
		if (SecurityUtil.isAdmin()) {
			reqUser.updateUserRole(request.role());
		}
		
		User updatedUser = userRepository.save(reqUser);
		return UserResponse.from(updatedUser);
	}

	// 회원 탈퇴
	@Override
	@Transactional
	public void withdrawUser(Long userId, HttpServletRequest request, HttpServletResponse response) {
		User reqUser = userRepository.findById(userId)
				.orElseThrow(() -> NotFoundUserException.EXCEPTION);
		User currentUser = SecurityUtil.getCurrentUser();
		
		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(reqUser.getUserId())) {
			throw NoAuthorizedUserException.EXCEPTION;
		}
		
		userRepository.delete(reqUser);
		
		Optional<Cookie> accessTokenCookie = CookieUtil.getCookie(request, "AT");
		if (accessTokenCookie.isPresent()) {
			String accessToken = accessTokenCookie.get().getValue();
			jwtTokenProvider.addAccessTokenToBlacklist(accessToken);
		}
		
		CookieUtil.clearAuthCookies(response);
		
		tokenRedisRepository.deleteById(String.valueOf(reqUser.getUserId()));
	}

	// 사용자 복구
	@Override
	@Transactional
	public UserResponse restoreUser(Long userId) {
		User user = userRepository.findDeletedByUserId(userId)
    			.orElseThrow(() -> NotFoundUserException.EXCEPTION);

		user.restoreUser();
		User saved = userRepository.save(user);

		return UserResponse.from(saved);
	}
}
