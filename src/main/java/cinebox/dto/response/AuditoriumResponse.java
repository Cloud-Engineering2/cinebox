package cinebox.dto.response;

import cinebox.entity.Auditorium;

public record AuditoriumResponse(
		Long auditoriumId,
		String auditoriumName
) {
	public static AuditoriumResponse from(Auditorium auditorium) {
		return new AuditoriumResponse(
				auditorium.getAuditoriumId(),
				auditorium.getName());
	}
}
