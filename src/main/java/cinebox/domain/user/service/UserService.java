package cinebox.domain.user.service;

import java.util.List;

import cinebox.domain.user.dto.UserResponse;
import cinebox.domain.user.dto.UserUpdateRequest;

public interface UserService {

	// 전체 사용자 조회
	List<UserResponse> getAllActiveUser();

	// 특정 사용자 조회
	UserResponse getUserById(Long userId);

	// 본인 사용자 정보 조회
	UserResponse getMyInform();

	// 사용자 정보 수정
	UserResponse updateUser(Long userId, UserUpdateRequest request);

	// 사용자 삭제
	void deleteUser(Long userId);

	// 사용자 복구
	UserResponse restoreUser(Long userId);

}
