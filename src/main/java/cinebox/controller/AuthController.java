package cinebox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import cinebox.dto.request.AuthRequest;
import cinebox.dto.request.UserRequest;
import cinebox.dto.response.AuthResponse;
import cinebox.dto.response.UserResponse;
import cinebox.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<UserResponse> signup(@RequestBody UserRequest user) {
		UserResponse newUser = userService.signup(user);
		return ResponseEntity.ok().body(newUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
		AuthResponse response = userService.login(authRequest);
		return ResponseEntity.ok(response);	
	}
	
	@GetMapping("/logout")
	public void logout(HttpServletRequest request) {
		// 클라이언트가 스토리지 내부에 토큰을 지우고 로그인 페이지로 리다이렉트
	}
}
