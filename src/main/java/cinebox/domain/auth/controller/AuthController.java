package cinebox.domain.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cinebox.domain.auth.dto.AuthRequest;
import cinebox.domain.auth.dto.AuthResponse;
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

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		authService.logout(request, response);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/callback")
	public ResponseEntity<AuthResponse> kakaoLogin(
			@RequestParam("code") String accessCode,
			HttpServletResponse httpServletResponse) {
		AuthResponse response = authService.oAuthLogin(accessCode, httpServletResponse);
		return ResponseEntity.ok(response);
	}
}
