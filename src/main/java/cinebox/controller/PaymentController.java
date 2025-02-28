package cinebox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.PaymentCancelRequest;
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

	// 결제 취소
	@PostMapping("/{paymentId}/cancel")
	public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable Long paymentId,
			@RequestBody PaymentCancelRequest cancelRequest) {

		// cancelRequest에 paymentId를 설정
		cancelRequest.setPaymentId(paymentId);

		// 결제 취소 서비스 호출
		PaymentResponse paymentResponse = paymentService.cancelPayment(cancelRequest);

		// 결제 취소 성공 응답
		return ResponseEntity.ok(paymentResponse);
	}

}
