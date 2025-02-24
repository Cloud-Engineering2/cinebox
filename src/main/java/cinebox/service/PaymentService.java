package cinebox.service;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.enums.PaymentMethod;
import cinebox.common.enums.PaymentStatus;
import cinebox.dto.request.PaymentRequest;
import cinebox.dto.response.PaymentResponse;
import cinebox.entity.Booking;
import cinebox.entity.Payment;
import cinebox.repository.BookingRepository;
import cinebox.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        log.info("예약 ID에 대한 결제 처리 : {}", request.getBookingId() );
    	
        // 예약 정보 조회
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> {
                    log.info("예매ID를 찾을 수 없습니다. : {}", request.getBookingId());
                    return new RuntimeException("예약을 찾을 수 없습니다.");
                });

        log.info("Found booking with ID: {}", booking.getBookingId());
        
        // PaymentMethod enum을 사용하여 결제 방식을 처리
        PaymentMethod method = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()); // 카드, 카카오페이, 네이버페이
        log.info("Payment method: {}", method);
        
        // 결제 상태 확인 후 처리 (실제 PG사 API 호출을 생략)
        PaymentStatus paymentStatus = PaymentStatus.COMPLETED; // 결제 성공 상태로 가정
        log.info("Payment status: {}", paymentStatus);
        
        // 결제 객체 생성
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(request.getTotalAmount())
                .method(method) // enum 값으로 결제 방식 지정
                .status(paymentStatus)
                .paidAt(LocalDateTime.now())  // 결제 완료 시간
                .build();

        // 결제 정보 저장
        paymentRepository.save(payment);
        log.info("Payment saved with ID: {}", payment.getPaymentId());
        
        // 예약 상태 업데이트 (결제 성공 시)
        booking.setStatus(BookingStatus.PAID);  // 결제 완료 후 예약 상태를 "PAID"로 변경
        // 예약 정보 저장
        bookingRepository.save(booking);
        log.info("Booking status updated to PAID for booking ID: {}", booking.getBookingId());


        // 결제 상태와 메시지 설정
        String message = "결제가 완료되었습니다.";
        String paymentMethod = method.name();  // 결제 방식 (카드, 카카오페이 등)

        // PaymentResponse 객체 생성하여 반환
        return new PaymentResponse(
                booking.getBookingId(),   // 예약 ID
                payment.getPaymentId(),   // 결제 ID
                payment.getAmount(), // 결제 금액
                paymentMethod,      // 결제 방식
                paymentStatus.name(), // 결제 상태
                message             // 결제 메시지
        );
    }
}
