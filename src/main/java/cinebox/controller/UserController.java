package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
import cinebox.security.JwtTokenProvider;
import cinebox.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	
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
	
	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody UserRequest userRequest, HttpServletRequest request) {
		String token = jwtTokenProvider.getToken(request);
		jwtTokenProvider.isUserMatchedWithToken(userRequest.getIdentifier(), token);
    	
	    User updatedUser = userService.updateUser(userRequest);
    	return ResponseEntity.ok().body(updatedUser);	 
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId, HttpServletRequest request) {
		String token = jwtTokenProvider.getToken(request);
		UserResponse user = userService.getUserById(userId);
		jwtTokenProvider.isUserMatchedWithToken(user.getIdentifier(), token);
		
		userService.deleteUser(userId);
		return ResponseEntity.ok().body("Success Delete");
	}
}
