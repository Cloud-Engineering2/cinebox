package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.UserUpdateRequest;
import cinebox.dto.response.UserResponse;
import cinebox.service.UserService;
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

	@PutMapping("/{userId}")
	public ResponseEntity<UserResponse> updateUser(
			@PathVariable("userId") Long userId,
			@RequestBody UserUpdateRequest request) {
		UserResponse response = userService.updateUser(userId, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.noContent().build();
	}
}
