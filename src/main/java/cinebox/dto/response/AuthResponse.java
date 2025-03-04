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
	
	private String accessToken;
	private String refreshToken;
}
