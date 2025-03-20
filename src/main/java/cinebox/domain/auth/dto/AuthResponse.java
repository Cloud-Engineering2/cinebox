package cinebox.domain.auth.dto;

public record AuthResponse(
		Long userId,
		String role,
		String idnetifier
) {
	
}