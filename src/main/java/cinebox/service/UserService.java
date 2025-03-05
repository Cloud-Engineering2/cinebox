package cinebox.service;

import java.util.List;

import cinebox.dto.request.UserRequest;
import cinebox.dto.response.UserResponse;
import cinebox.entity.User;

public interface UserService {

	// 전체 사용자 조회
	List<UserResponse> getAllUser();

	// 특정 사용자 조회
	UserResponse getUserById(Long userId);

	// 사용자 정보 수정
	User updateUser(Long userId, UserRequest request);

	// 사용자 삭제
	void deleteUser(Long userId);

}
