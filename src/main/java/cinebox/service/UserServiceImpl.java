package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.Role;
import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.dto.request.UserRequest;
import cinebox.dto.response.UserResponse;
import cinebox.entity.User;
import cinebox.repository.UserRepository;
import cinebox.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	// deleted = true 인 경우 조회되지 않는다.
	@Override
	@Transactional(readOnly = true)
	public List<UserResponse> getAllUser() {
		return userRepository.findAllByIsDeletedFalse().stream().map(UserResponse::from).collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
    public UserResponse getUserById(Long userId) {
    	User user = userRepository.findByUserIdAndIsDeletedFalse(userId).orElseThrow(() -> NotFoundUserException.EXCEPTION);
        return UserResponse.from(user);
    }

	@Override
	@Transactional
	public User updateUser(Long requestUserId, UserRequest userRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
		Long currentUserId = userDetails.getUser().getUserId();
		Role currentUserRole = userDetails.getUser().getRole();
		
		User requestUser = userRepository.findByUserIdAndIsDeletedFalse(requestUserId).orElseThrow(() -> NotFoundUserException.EXCEPTION);
		
		boolean isUser = !(currentUserRole.equals(Role.ADMIN));
		boolean isMatchedUser = (requestUserId == currentUserId);
		boolean isRoleChangeByUser = (requestUser.getRole().equals(Role.USER) && !userRequest.getRole().equals(Role.USER));
		
		// User 레벨의 사용자가 Role이 User가 아닌 다른 역할로 업데이트를 하려는 경우
		if(isUser && (!isMatchedUser || isRoleChangeByUser)) {
			System.out.println("User 레벨의 사용자가 역할 변경을 시도하였습니다.");
			throw NoAuthorizedUserException.EXCEPTION;
		}
		
		if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
			String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
			userRequest.setPassword(encodedPassword);
		}
        
    	User updateUser = User.builder()
						.userId(requestUserId)
						.identifier(userRequest.getIdentifier() != null ? userRequest.getIdentifier() : requestUser.getIdentifier())
						.email(userRequest.getEmail() != null ? userRequest.getEmail() : requestUser.getEmail())
						.password(userRequest.getPassword() != null ? userRequest.getPassword() : requestUser.getPassword())
						.name(userRequest.getName() != null ? userRequest.getName() : requestUser.getName())
						.phone(userRequest.getPhone() != null ? userRequest.getPhone() : requestUser.getPhone())
						.age(userRequest.getAge() != null ? userRequest.getAge() : requestUser.getAge())
						.gender(userRequest.getGender() != null ? userRequest.getGender() : requestUser.getGender())
						.role(userRequest.getRole() != null ? userRequest.getRole() : requestUser.getRole())
						.build();
    	User user = userRepository.save(updateUser);
    	return user;
	}

	@Override
	@Transactional
	public void deleteUser(Long userId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
		Long currentUserId = userDetails.getUser().getUserId();

        if (currentUserId.equals(userId)) {
        	userRepository.deleteById(userId);
        }
	}
}
