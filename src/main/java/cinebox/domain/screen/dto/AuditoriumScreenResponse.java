package cinebox.domain.screen.dto;

import java.util.List;

public record AuditoriumScreenResponse(
		Long auditoriumId,
		String auditoriumName,
		List<ScreenResponse> screens
) {
	public static AuditoriumScreenResponse from(List<ScreenResponse> screens) {
		if (screens == null || screens.isEmpty()) {
			throw new IllegalArgumentException("Empty Screen List");
		}
		
		ScreenResponse response = screens.get(0);
		return new AuditoriumScreenResponse(
				response.auditoriumId(),
				response.auditoriumName(),
				screens
		);
	}
}
