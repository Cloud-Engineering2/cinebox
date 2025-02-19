package cinebox.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import cinebox.dto.UserDTO;
import cinebox.entity.User;
import cinebox.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
		try {
			User user = userService.signup(userDTO);
			return ResponseEntity.ok().body(user);
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
		try {
			String token = userService.login(userDTO);

			System.out.println("✅ 로그인 성공! - Controller : " + token);

			// 토큰 주입
			Map<String, String> response = new HashMap<>();
			response.put("token", token);
			response.put("identifier", userDTO.getIdentifier());
			response.put("role", userDTO.getRole().toString());

			return ResponseEntity.ok(response);	
		} catch (Exception ex) {
			return ResponseEntity.badRequest().body(ex.getMessage());
		}
	}
	
	@PostMapping("/logout")
	public String logout(HttpServletRequest request) {
		// 스토리지 내부에 토큰을 프론트에서 지우고 로그인 페이지로 리다이렉트
		return "Logout";
	}
}
