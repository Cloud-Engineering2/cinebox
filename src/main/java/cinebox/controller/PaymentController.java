package cinebox.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.common.exception.booking.NotFoundBookingException;
import cinebox.common.exception.payment.NotFoundPaymentException;
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
    public ResponseEntity<PaymentResponse> cancelPayment(
        @PathVariable Long paymentId,  // URL 경로에서 paymentId를 추출
        @RequestBody PaymentCancelRequest cancelRequest) {
        
        try {
            // cancelRequest에 paymentId를 설정
            cancelRequest.setPaymentId(paymentId);
            
            // 결제 취소 서비스 호출
            PaymentResponse paymentResponse = paymentService.cancelPayment(cancelRequest);

            // 결제 취소 성공
            if ("REFUNDED".equals(paymentResponse.getPaymentStatus())) {
                return ResponseEntity.ok(paymentResponse);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(paymentResponse);
            }
        } catch (NotFoundPaymentException e) {
            log.error("Payment not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new PaymentResponse(null, null, null, null, "FAILED", "결제 정보가 없습니다."));
        } catch (NotFoundBookingException e) {
            log.error("Booking not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new PaymentResponse(null, null, null, null, "FAILED", "예매 정보가 없습니다."));
        } catch (Exception e) {
            log.error("Error processing cancel payment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new PaymentResponse(null, null, null, null, "FAILED", "결제 취소 중 오류가 발생했습니다."));
        }
    }
   

   
    
}
