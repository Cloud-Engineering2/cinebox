package cinebox.domain.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.domain.user.dto.UserResponse;
import cinebox.domain.user.dto.UserUpdateRequest;
import cinebox.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// 전체 사용자 조회
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllActiveUser() {
		log.info("전체 활성 사용자 조회 요청");
		List<UserResponse> responses = userService.getAllActiveUser();
		log.info("전체 활성 사용자 조회 완료, 결과 수: {}", responses.size());
		return ResponseEntity.ok(responses);
	}

	// 사용자 정보 조회
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserResponse> getUser(@PathVariable("userId") Long userId) {
		log.info("사용자 정보 조회 요청: userId={}", userId);
		UserResponse response = userService.getUserById(userId);
		log.info("사용자 정보 조회 완료: userId={}", userId);
		return ResponseEntity.ok(response);
	}

	// 본인 사용자 정보 조회
	@GetMapping("/my")
	public ResponseEntity<UserResponse> getMyInform() {
		log.info("본인 사용자 정보 조회 요청");
		UserResponse response = userService.getMyInform();
		log.info("본인 사용자 정보 조회 완료: userId={}", response.userId());
		return ResponseEntity.ok(response);
	}

	// 사용자 정보 수정
	@PutMapping("/{userId}")
	public ResponseEntity<UserResponse> updateUser(
			@PathVariable("userId") Long userId,
			@RequestBody @Validated UserUpdateRequest request) {
		log.info("사용자 정보 수정 요청: userId={}", userId);
		UserResponse response = userService.updateUser(userId, request);
		log.info("사용자 정보 수정 완료: userId={}", userId);
		return ResponseEntity.ok(response);
	}

	// 사용자 회원 탈퇴
	@PostMapping("/{userId}/withdraw")
	public ResponseEntity<Void> withdrawUser(
			@PathVariable("userId") Long userId,
			HttpServletRequest request,
			HttpServletResponse response) {
		log.info("사용자 탈퇴 요청: userId={}", userId);
		userService.withdrawUser(userId, request, response);
		log.info("사용자 탈퇴 처리 완료: userId={}", userId);
		return ResponseEntity.noContent().build();
	}

	// 사용자 복구
	@PostMapping("/{userId}/restore")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserResponse> restoreUser(@PathVariable("userId") Long userId) {
		log.info("사용자 복구 요청: userId={}", userId);
		UserResponse response = userService.restoreUser(userId);
		log.info("사용자 복구 완료: userId={}", userId);
		return ResponseEntity.ok(response);
	}
}
