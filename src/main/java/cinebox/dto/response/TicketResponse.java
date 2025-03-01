package cinebox.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.Booking;
import cinebox.entity.BookingSeat;
import cinebox.entity.Screen;
import lombok.Builder;

@Builder
public record TicketResponse(
		Long bookingId,
		Long movieId,
		Long auditoriumId,
		String movieTitle,
		String auditoriumName,
		String posterImageUrl,
		BigDecimal totPrice,
		List<String> seatNumbers,
		LocalDateTime screenStartTime,
		LocalDateTime screenEndTime,
		LocalDateTime bookingAt,
		BookingStatus status
) {
	public static TicketResponse from(Booking booking) {
		List<BookingSeat> bookingSeats = booking.getBookingSeats();
		List<String> seatNumbers = bookingSeats.stream()
				.map(bs -> bs.getSeat().getSeatNumber())
				.collect(Collectors.toList());

		Screen screen = bookingSeats.get(0).getScreen();
		String poster = screen.getMovie().getPosterImageUrl();

		return new TicketResponse(
				booking.getBookingId(),
				screen.getMovie().getMovieId(),
				screen.getAuditorium().getAuditoriumId(),
				screen.getMovie().getTitle(),
				screen.getAuditorium().getName(),
				poster,
				booking.getTotalPrice(),
				seatNumbers,
				screen.getStartTime(),
				screen.getEndTime(),
				booking.getBookingDate(),
				booking.getStatus());
	}
}
