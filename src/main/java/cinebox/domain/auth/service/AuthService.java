package cinebox.domain.auth.service;

import cinebox.domain.auth.dto.AuthRequest;
import cinebox.domain.auth.dto.AuthResponse;
import cinebox.domain.auth.dto.SignUpRequest;
import cinebox.domain.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

	// 회원가입
	UserResponse signup(SignUpRequest request);

	// 카카오 회원가입
	UserResponse kakaoSignup(SignUpRequest request);

	// 로그인
	AuthResponse login(AuthRequest request, HttpServletResponse response);

	// 로그아웃
	void logout(HttpServletRequest request, HttpServletResponse response);

	// 카카오 로그인
	Object oAuthLogin(String accessCode, HttpServletResponse httpServletResponse);

}
