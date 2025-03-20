package cinebox.domain.user.service;

import java.util.List;

import cinebox.domain.user.dto.UserResponse;
import cinebox.domain.user.dto.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

	// 전체 사용자 조회
	List<UserResponse> getAllActiveUser();

	// 특정 사용자 조회
	UserResponse getUserById(Long userId);

	// 본인 사용자 정보 조회
	UserResponse getMyInform();

	// 사용자 정보 수정
	UserResponse updateUser(Long userId, UserUpdateRequest request);

	// 회원 탈퇴
	void withdrawUser(Long userId, HttpServletRequest request, HttpServletResponse response);

	// 사용자 복구
	UserResponse restoreUser(Long userId);

}
