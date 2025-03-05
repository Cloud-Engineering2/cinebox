package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.dto.request.UserUpdateRequest;
import cinebox.dto.response.UserResponse;
import cinebox.entity.User;
import cinebox.repository.UserRepository;
import cinebox.security.PrincipalDetails;
import cinebox.security.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

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
			reqUser.updateUserRole(request);
		}
		
		User updatedUser = userRepository.save(reqUser);
		return UserResponse.from(updatedUser);
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) {
		User reqUser = userRepository.findById(userId)
				.orElseThrow(() -> NotFoundUserException.EXCEPTION);
		User currentUser = SecurityUtil.getCurrentUser();
		
		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(reqUser.getUserId())) {
			throw NoAuthorizedUserException.EXCEPTION;
		}
		
		userRepository.delete(reqUser);
	}
}
