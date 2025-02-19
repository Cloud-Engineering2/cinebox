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

import lombok.RequiredArgsConstructor;
import cinebox.dto.UserDTO;
import cinebox.service.UserService;

@RestController
@RequestMapping(path = "/api/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@GetMapping()
	public ResponseEntity<Object> getAllUser() {

		try {
			List<UserDTO> users = userService.getAllUser();
			return ResponseEntity.ok().body(users);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<Object> getUser(@PathVariable("userId") Long userId) {

		try {
			UserDTO user = userService.getUserById(userId);
			return ResponseEntity.ok().body(user);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@PutMapping()
	public ResponseEntity<Object> updateUser(@RequestBody UserDTO userDTO) {

		try {
			userService.updateUser(userDTO);
			return ResponseEntity.ok().body("Success Update");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<Object> deleteUser(@PathVariable("userId") Long userId) {

		try {
			userService.deleteUser(userId);
			return ResponseEntity.ok().body("Success Delete User");
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
}
