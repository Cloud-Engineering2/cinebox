package cinebox.dto.response;

public record SeatResponse(
		Long seatId,
		String seatNumber,
		boolean reserved
) {
}