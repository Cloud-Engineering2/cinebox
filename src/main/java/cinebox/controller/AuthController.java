package cinebox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.AuthRequest;
import cinebox.dto.request.SignUpRequest;
import cinebox.dto.response.AuthResponse;
import cinebox.dto.response.UserResponse;
import cinebox.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<UserResponse> signup(@RequestBody @Validated SignUpRequest user) {
		UserResponse newUser = authService.signup(user);
		return ResponseEntity.ok().body(newUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@RequestBody AuthRequest authRequest,
			HttpServletResponse response) {
		AuthResponse authResponse = authService.login(authRequest, response);
		return ResponseEntity.ok(authResponse);	
	}
	
	@GetMapping("/logout")
	public void logout(HttpServletRequest request) {
		// 클라이언트가 스토리지 내부에 토큰을 지우고 로그인 페이지로 리다이렉트
	}
}
