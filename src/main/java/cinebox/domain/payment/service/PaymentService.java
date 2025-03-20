package cinebox.domain.payment.service;

import cinebox.domain.payment.dto.PaymentRequest;
import cinebox.domain.payment.dto.PaymentResponse;

public interface PaymentService {

	// 결제 진행
	PaymentResponse processPayment(PaymentRequest request);

	/**
	 * @deprecated
	 * Booking에서 처리 
	 */
	// 결제 취소
	@Deprecated
	PaymentResponse cancelPayment(Long paymentId);
}
