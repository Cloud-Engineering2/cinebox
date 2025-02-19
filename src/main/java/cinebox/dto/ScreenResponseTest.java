package cinebox.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import cinebox.entity.Seat;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ScreenResponseTest {

    private Long screenId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private List<SeatDTO> availableSeats; // 예매 가능한 좌석

    // 생성자
    public ScreenResponseTest(Long screenId, LocalDateTime startTime, LocalDateTime endTime, BigDecimal price,List<SeatDTO> availableSeats) {
        this.screenId = screenId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.availableSeats=availableSeats;
    }

 
}
