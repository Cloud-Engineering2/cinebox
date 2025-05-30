package cinebox.domain.screen.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinebox.domain.auditorium.entity.Auditorium;
import cinebox.domain.screen.entity.Screen;

public record ScreenResponse(
		Long screenId,
		Long movieId,
		String movieTitle,
		Long auditoriumId,
		String auditoriumName,
		Integer auditoriumCapacity,
		Integer auditoriumRemain,
		LocalDateTime startTime,
		LocalDateTime endTime,
		BigDecimal price
) {
	public static ScreenResponse from(Screen screen) {
		Auditorium auditorium = screen.getAuditorium();
		
		int capacity = auditorium.getCapacity();
		int booked = screen.getBookingSeats() != null ? screen.getBookingSeats().size() : 0;
		int remain = capacity - booked; 
		
		return new ScreenResponse(
				screen.getScreenId(),
				screen.getMovie().getMovieId(),
				screen.getMovie().getTitle(),
				auditorium.getAuditoriumId(),
				auditorium.getName(),
				capacity,
				remain,
				screen.getStartTime(),
				screen.getEndTime(),
				screen.getPrice()
		);
	}
}