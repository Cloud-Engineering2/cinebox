package cinebox.service;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.enums.PaymentMethod;
import cinebox.common.enums.PaymentStatus;
import cinebox.common.exception.ExceptionMessage;
import cinebox.common.exception.payment.AlreadyPaidException;
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
        
        // 예약 정보 조회
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> {
                    log.info("예매ID를 찾을 수 없습니다. : {}", request.getBookingId());
                    return new RuntimeException("예약을 찾을 수 없습니다.");
                });

        log.info("예약 ID에 대한 결제 처리 : {}", request.getBookingId());

      
        // 중복 결제 방지: 이미 결제된 예약인지 확인
        if (booking.getStatus() == BookingStatus.PAID) {
            log.warn("이미 결제된 예매입니다. 예매 ID: {}", booking.getBookingId());
            throw new AlreadyPaidException(ExceptionMessage.SEAT_ALREADY_PAYMENT);
        }

        
        // PaymentMethod enum을 사용하여 결제 방식을 처리
        PaymentMethod method = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        log.info("Payment method: {}", method);

        // 결제 상태 확인 후 처리 (실제 PG사 API 호출을 생략)
        PaymentStatus paymentStatus = PaymentStatus.COMPLETED; // 결제 성공 상태로 가정
        log.info("Payment status: {}", paymentStatus);

        // 결제 객체 생성
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(request.getTotalAmount())
                .method(method)
                .status(paymentStatus)
                .paidAt(LocalDateTime.now())
                .build();

        // 결제 정보 저장
        paymentRepository.save(payment);
        log.info("Payment saved with ID: {}", payment.getPaymentId());

        
        // 예약 상태 업데이트 (결제 성공 시)
        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);
        log.info("Booking status updated to PAID for booking ID: {}", booking.getBookingId());

        
        
        // PaymentResponse 객체 반환
        return new PaymentResponse(
                booking.getBookingId(),
                payment.getPaymentId(),
                payment.getAmount(),
                method.name(),
                paymentStatus.name(),
                "결제가 완료되었습니다."
        );
    }

    
    
}