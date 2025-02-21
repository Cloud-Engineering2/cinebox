package cinebox.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.Booking;
import cinebox.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class BookingRequest {
	
	private Long userId; // userID
    private List<Long> seatIds; // 선택된 좌석 ID 리스트
    private BigDecimal totalPrice; // 총 가격
    private List<Long> paymentIds; // 결제 ID 리스트
    
    public BookingRequest(Long userId, List<Long> seatIds, BigDecimal totalPrice, List<Long> paymentIds) {
        this.userId = userId;
        this.seatIds = seatIds;
        this.totalPrice = totalPrice;
        this.paymentIds = paymentIds;
    }
    
    // 요청 DTO → 엔티티 변환 메서드 (Booking 엔티티로 변환)
    public Booking toEntity(User user) {
        return Booking.builder()
                .user(user)
                .totalPrice(this.totalPrice)
                .status(BookingStatus.PENDING)  // 예약 상태는 기본적으로 PENDING으로 설정
                .bookingDate(LocalDateTime.now())  // 현재 시간으로 예약 일자 설정
                .build();
    }
    
   
    
}
