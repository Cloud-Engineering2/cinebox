package cinebox.domain.screen.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record DateScreenResponse(
		LocalDate date,
		List<AuditoriumScreenResponse> auditoriums
) {
	public static DateScreenResponse from(LocalDate date, List<ScreenResponse> screens) {
		Map<Long, List<ScreenResponse>> groupedByAuditorium = screens.stream()
				.collect(Collectors.groupingBy(ScreenResponse::auditoriumId));
		
		List<AuditoriumScreenResponse> auditoriumScreens = groupedByAuditorium.values().stream()
				.map(AuditoriumScreenResponse::from)
				.collect(Collectors.toList());
		
		return new DateScreenResponse(date, auditoriumScreens);
	}
}
