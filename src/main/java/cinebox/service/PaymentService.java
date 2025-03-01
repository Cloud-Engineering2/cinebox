package cinebox.service;

import cinebox.dto.request.PaymentRequest;
import cinebox.dto.response.PaymentResponse;

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
