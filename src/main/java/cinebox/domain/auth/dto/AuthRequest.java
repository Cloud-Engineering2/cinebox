package cinebox.domain.auth.dto;

public record AuthRequest(
		String identifier,
		String password
) {
	
}