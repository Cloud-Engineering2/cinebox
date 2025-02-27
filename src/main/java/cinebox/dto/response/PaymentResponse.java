package cinebox.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentResponse {

	private Long bookingId; // 예약 ID
	private Long paymentId; // 결제 ID
	private BigDecimal amount; // 결제 금액
	private String paymentMethod; // 결제 방법
	private String paymentStatus; // 결제 상태 (예: 완료, 실패)
	private String message; // 메시지 (결제 완료 여부 등)


	 // 주요 필드만 받는 생성자 (기본 성공 응답용)
    public PaymentResponse(Long bookingId, Long paymentId, BigDecimal amount, String paymentMethod) {
        this(bookingId, paymentId, amount, paymentMethod, "COMPLETED", "결제가 완료되었습니다.");
    }
    
    // 기본 생성자에 null 허용
    public PaymentResponse(Long bookingId, Long paymentId, BigDecimal amount, String paymentMethod, String paymentStatus, String message) {
        this.bookingId = bookingId;
        this.paymentId = paymentId;
        this.amount = amount != null ? amount : BigDecimal.ZERO;  // 기본값 설정
        this.paymentMethod = paymentMethod != null ? paymentMethod : "Unknown";  // 기본값 설정
        this.paymentStatus = paymentStatus != null ? paymentStatus : "UNKNOWN";
        this.message = message != null ? message : "No message provided.";
    }
  
    
}
