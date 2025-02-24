package cinebox.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

	  	private Long bookingId;  // 예약 ID
	    private BigDecimal totalAmount;  // 결제 금액
	    private String paymentMethod;   // 결제 방법 (CARD, KAKAO_PAY, NAVER_PAY)
}
