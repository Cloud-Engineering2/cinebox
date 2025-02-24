package cinebox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.PaymentRequest;
import cinebox.dto.response.PaymentResponse;
import cinebox.service.PaymentService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
        // 결제 처리 로직 (예: 결제 API 호출 등)
        Long paymentId = 12345L;  // 예시 결제 ID (Long 타입)
        Long bookingId = request.getBookingId();  // 결제와 연관된 예약 ID (Long 타입)
        BigDecimal amount = request.getTotalAmount();  // 이미 BigDecimal 타입이라 그대로 사용
        String paymentMethod = "Credit Card";  // 결제 방식 (예시: 카드 결제)
        
        // PaymentResponse 객체 생성
        PaymentResponse paymentResponse = new PaymentResponse(bookingId, paymentId, amount, paymentMethod);

        // 응답 객체 반환
        return ResponseEntity.ok(paymentResponse);
    }
    
    
}
