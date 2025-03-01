package cinebox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.PaymentRequest;
import cinebox.dto.response.PaymentResponse;
import cinebox.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

	private final PaymentService paymentService;

	// 결제하기
	@PostMapping
	public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
		PaymentResponse paymentResponse = paymentService.processPayment(request);
		return ResponseEntity.ok(paymentResponse);
	}

	/**
	 * @deprecated
	 * Booking에서 처리 
	 */
	// 결제 취소
	@Deprecated
	@PostMapping("/{paymentId}/cancel")
	public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable("paymentId") Long paymentId) {
		PaymentResponse paymentResponse = paymentService.cancelPayment(paymentId);
		return ResponseEntity.ok(paymentResponse);
	}
}
