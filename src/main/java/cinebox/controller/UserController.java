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

import cinebox.dto.request.UserRequest;
import cinebox.dto.response.UserResponse;
import cinebox.entity.User;
import cinebox.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUser() {
		List<UserResponse> users = userService.getAllUser();
		return ResponseEntity.ok().body(users);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable("userId") Long userId) {
		UserResponse user = userService.getUserById(userId);
		return ResponseEntity.ok().body(user);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId, @RequestBody UserRequest userRequest, HttpServletRequest request) {    	
	    User updatedUser = userService.updateUser(userId, userRequest);
    	return ResponseEntity.ok().body(updatedUser);	 
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {		
		userService.deleteUser(userId);
		return ResponseEntity.ok().body("Success Delete");
	}
}
