package cinebox.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
	
    private Long screenId;
    private List<String> seatIds; // 좌석번호 (예: ["A1", "A2", "B1"])
    private LocalDateTime startTime; // 상영 시작 시간 추가
    private Long userId;  // 예매하는 사용자 ID
    

}
