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
import lombok.extern.slf4j.Slf4j;



@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    
    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
        PaymentResponse paymentResponse = paymentService.processPayment(request);
        return ResponseEntity.ok(paymentResponse); 
    }
    
//    // 결제확인
//    @PostMapping("/confirm")
//    public ResponseEntity<?> confirmPayment(@RequestBody PaymentConfirmRequest request) {
//        Payment payment = paymentService.verifyPayment(request);
//        bookingService.updateBookingStatus(payment.getBookingId(), "PAID");
//        return ResponseEntity.ok("결제 확인 완료");
//    }
    
}
