package cinebox.domain.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	// 전체 사용자 조회
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserResponse>> getAllActiveUser() {
		List<UserResponse> responses = userService.getAllActiveUser();
		return ResponseEntity.ok(responses);
	}

	// 사용자 정보 조회
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserResponse> getUser(@PathVariable("userId") Long userId) {
		UserResponse response = userService.getUserById(userId);
		return ResponseEntity.ok(response);
	}

	// 본인 사용자 정보 조회
	@GetMapping("/my")
	public ResponseEntity<UserResponse> getMyInform() {
		UserResponse response = userService.getMyInform();
		return ResponseEntity.ok(response);
	}

	// 사용자 정보 수정
	@PutMapping("/{userId}")
	public ResponseEntity<UserResponse> updateUser(
			@PathVariable("userId") Long userId,
			@RequestBody @Validated UserUpdateRequest request) {
		UserResponse response = userService.updateUser(userId, request);
		return ResponseEntity.ok(response);
	}

	// 사용자 회원 탈퇴
	@PostMapping("/{userId}/withdraw")
	public ResponseEntity<Void> withdrawUser(
			@PathVariable("userId") Long userId,
			HttpServletRequest request,
			HttpServletResponse response) {
		userService.withdrawUser(userId, request, response);
		return ResponseEntity.noContent().build();
	}

	// 사용자 복구
	@PostMapping("/{userId}/restore")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<UserResponse> restoreUser(@PathVariable("userId") Long userId) {
		UserResponse response = userService.restoreUser(userId);
		return ResponseEntity.ok(response);
	}
}
