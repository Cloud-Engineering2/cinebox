package cinebox.domain.auditorium.dto;

import cinebox.domain.auditorium.entity.Auditorium;

public record AuditoriumResponse(
		Long auditoriumId,
		String auditoriumName,
		Integer auditoriumCapacity
) {
	public static AuditoriumResponse from(Auditorium auditorium) {
		return new AuditoriumResponse(
				auditorium.getAuditoriumId(),
				auditorium.getName(),
				auditorium.getCapacity());
	}
}
