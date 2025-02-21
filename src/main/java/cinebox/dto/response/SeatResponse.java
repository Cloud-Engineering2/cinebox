package cinebox.dto.response;

import cinebox.entity.Seat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder 
public class SeatResponse {
    private Long seatId;
    private String seatNumber;
    private Long auditoriumId;  // 상영관 ID
    private boolean available; // 좌석 사용 가능 여부

    // 생성자 추가
    public SeatResponse(Long seatId, String seatNumber,Long auditorium, boolean available) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.auditoriumId = auditorium;
        this.available = available;
    }
    
 
    // Seat 객체를 받아서 초기화하는 생성자 추가
    public SeatResponse(Seat seat) {
        this.seatId = seat.getSeatId();
        this.seatNumber = seat.getSeatNumber();
        this.auditoriumId = (seat.getAuditorium() != null) ? seat.getAuditorium().getAuditoriumId() : null;  // ID만 저장
        
     // 예매되지 않은 좌석만 available = true로 설정
        this.available = seat.getBookingSeats().isEmpty();  // 예약된 좌석이 없으면 available = true
    }

}