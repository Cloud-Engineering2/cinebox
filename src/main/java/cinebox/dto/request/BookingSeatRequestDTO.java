package cinebox.dto.request;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.BookingSeat;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookingSeatRequestDTO {
    private Long seatId;
    private Long bookingId;
    private Long screenId;
    private String seatNumber;

    // DTO -> 엔티티 변환 (Request -> Entity)
    public BookingSeat toEntity(Screen screen, Seat seat) {
        return BookingSeat.builder()
            .screen(screen)
            .seat(seat)
            .status(BookingStatus.PENDING)
            .build();
    }
}
