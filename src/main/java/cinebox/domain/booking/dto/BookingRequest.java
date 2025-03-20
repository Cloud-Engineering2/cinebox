package cinebox.domain.booking.dto;

import java.util.List;

public record BookingRequest(
		Long screenId,
		List<String> seatNumbers
) {
	
}