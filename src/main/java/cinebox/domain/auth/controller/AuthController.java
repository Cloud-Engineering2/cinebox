package cinebox.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cinebox.common.validation.CreateGroup;
import cinebox.domain.auth.dto.AuthRequest;
import cinebox.domain.auth.dto.AuthResponse;
import cinebox.domain.auth.dto.KakaoProfile;
import cinebox.domain.auth.dto.SignUpRequest;
import cinebox.domain.auth.service.AuthService;
import cinebox.domain.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<UserResponse> signup(@RequestBody @Validated SignUpRequest request) {
		UserResponse response = authService.signup(request);
		return ResponseEntity.ok(response);
	}

	// 카카오 회원가입
	@PostMapping("/signup/kakao")
	public ResponseEntity<UserResponse> kakaoSignup(@RequestBody @Validated(CreateGroup.class) SignUpRequest request) {
		UserResponse response = authService.kakaoSignup(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@RequestBody AuthRequest authRequest,
			HttpServletResponse response) {
		AuthResponse authResponse = authService.login(authRequest, response);
		return ResponseEntity.ok(authResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		authService.logout(request, response);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/callback")
	public ResponseEntity<?> kakaoLogin(
			@RequestParam("code") String accessCode,
			HttpServletResponse httpServletResponse) {
		Object response = authService.oAuthLogin(accessCode, httpServletResponse);
		
		if (response instanceof KakaoProfile) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/callback/test")
	public ResponseEntity<?> test() {
		return ResponseEntity.ok().build();
	}
}
