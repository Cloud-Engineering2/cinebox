package cinebox.dto;

import cinebox.entity.Booking;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingSeatDTO {
    private Long bookingSeatId;
    private Booking booking;
    private Screen screen;
    private Seat seat;
}
