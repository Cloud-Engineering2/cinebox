package cinebox.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record BookingRequest(
    Long screenId,
    List<Long> seatIds,
    BigDecimal totalPrice
) {

}
