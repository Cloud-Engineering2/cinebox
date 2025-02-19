package cinebox.dto.request;

import cinebox.entity.Seat;
import lombok.Data;

@Data
public class SeatRequestDTO {
	
	private Long seatId;
    private String seatNumber;

    // 생성자
    public SeatRequestDTO(Long seatId, String seatNumber) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
    }

    // DTO -> 엔티티 변환
    public Seat toEntity() {
        return Seat.builder()
                   .seatId(this.seatId)
                   .seatNumber(this.seatNumber)
                   .build();
    }
    
    

}
