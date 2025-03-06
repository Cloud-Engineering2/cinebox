package cinebox.domain.seat.dto;

public record SeatResponse(
		Long seatId,
		String seatNumber,
		boolean reserved
) {
}