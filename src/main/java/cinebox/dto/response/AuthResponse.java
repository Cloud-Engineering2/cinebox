package cinebox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
//	private Long userId;
//    private String identifier;
//    private String role;
//    private String token;
	
	private Long userId;
	private String role;
	private String identifier;
	private String accessToken;
	private String refreshToken;
}
