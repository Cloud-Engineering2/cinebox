package cinebox.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(
    Long bookingId,
    LocalDateTime bookingDate,
    Long screenId,
    List<Long> seatIds,
    String status
) {

}
