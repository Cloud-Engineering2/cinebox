package cinebox.service;

import cinebox.dto.request.PaymentCancelRequest;
import cinebox.dto.request.PaymentRequest;
import cinebox.dto.response.PaymentResponse;

public interface PaymentService {

	// 결제 진행
	PaymentResponse processPayment(PaymentRequest request);

	// 결제 취소
	PaymentResponse cancelPayment(PaymentCancelRequest request);
}
