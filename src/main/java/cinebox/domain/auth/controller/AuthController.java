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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<UserResponse> signup(@RequestBody @Validated SignUpRequest request) {
		log.info("회원가입 요청 수신: identifier={}, email={}", request.identifier(), request.email());
		UserResponse response = authService.signup(request);
		log.info("회원가입 성공: userId={}", response.userId());
		return ResponseEntity.ok(response);
	}

	// 카카오 회원가입
	@PostMapping("/signup/kakao")
	public ResponseEntity<UserResponse> kakaoSignup(@RequestBody @Validated(CreateGroup.class) SignUpRequest request) {
		log.info("카카오 회원가입 요청 수신: identifier={}, email={}", request.identifier(), request.email());
		UserResponse response = authService.kakaoSignup(request);
		log.info("카카오 회원가입 성공: userId={}", response.userId());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@RequestBody AuthRequest authRequest,
			HttpServletResponse response) {
		log.info("로그인 요청 수신: identifier={}", authRequest.identifier());
		AuthResponse authResponse = authService.login(authRequest, response);
		log.info("로그인 성공: userId={}", authResponse.userId());
		return ResponseEntity.ok(authResponse);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		log.info("로그아웃 요청 수신");
		authService.logout(request, response);
		log.info("로그아웃 처리 완료");
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/callback")
	public ResponseEntity<?> kakaoLogin(
			@RequestParam("code") String accessCode,
			HttpServletResponse httpServletResponse) {
		log.info("카카오 로그인 콜백 요청 수신: code={}", accessCode);
		Object response = authService.oAuthLogin(accessCode, httpServletResponse);
		
		if (response instanceof KakaoProfile) {
			log.warn("카카오 프로필 응답: 인증 실패");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
		log.info("카카오 로그인 성공");
		return ResponseEntity.ok(response);
	}
}
