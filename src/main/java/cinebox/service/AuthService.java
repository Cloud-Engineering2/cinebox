package cinebox.service;

import cinebox.dto.request.AuthRequest;
import cinebox.dto.request.UserRequest;
import cinebox.dto.response.AuthResponse;
import cinebox.dto.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

	// 회원가입
	UserResponse signup(UserRequest request);

	// 로그인
	AuthResponse login(AuthRequest request, HttpServletResponse response);
}
