package cinebox.dto.request;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class BookingRequest {
	
	private Long userId; // userID
	private Long screenId; // 예매할 상영회차Id
    private List<Long> seatIds; // 선택된 좌석 ID 리스트
  
    private List<Long> paymentIds; // 결제 ID 리스트
    
    public BookingRequest(Long userId, Long screenId,  List<Long> seatIds,  List<Long> paymentIds) {
        this.userId = userId;
        this.seatIds = seatIds;
        this.screenId= screenId;
        this.paymentIds = paymentIds;
    }
    
 
    
}
