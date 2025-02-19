package cinebox.dto.response;

import cinebox.entity.BookingSeat;
import cinebox.entity.Seat;
import cinebox.entity.Screen;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingSeatResponseDTO {
    private Long seatId;
    private Long bookingId;
    private Long screenId;
    private String seatNumber;

    // 엔티티 -> DTO 변환 (Entity -> Response)
    public static BookingSeatResponseDTO fromEntity(BookingSeat bookingSeat) {
        BookingSeatResponseDTO dto = new BookingSeatResponseDTO();
        dto.setSeatId(bookingSeat.getSeat().getSeatId());
        dto.setBookingId(bookingSeat.getBooking().getBookingId());
        dto.setScreenId(bookingSeat.getScreen().getScreenId());
        dto.setSeatNumber(bookingSeat.getSeat().getSeatNumber());
        return dto;
    }
}

