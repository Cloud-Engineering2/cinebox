package cinebox.dto.response;

import cinebox.entity.Seat;
import lombok.Data;

@Data
public class SeatResponseDTO {
	
	private Long seatId;
    private String seatNumber;

    // 생성자
    public SeatResponseDTO(Long seatId, String seatNumber) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
    }

    // 엔티티 -> DTO 변환
    public static SeatResponseDTO from(Seat seat) {
        return new SeatResponseDTO(seat.getSeatId(), seat.getSeatNumber());
    }

}
