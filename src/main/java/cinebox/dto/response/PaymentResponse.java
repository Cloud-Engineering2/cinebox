package cinebox.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    
}
