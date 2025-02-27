package cinebox.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.enums.PaymentMethod;
import cinebox.common.enums.PaymentStatus;
import cinebox.common.exception.ExceptionMessage;
import cinebox.common.exception.booking.NotFoundBookingException;
import cinebox.common.exception.payment.AlreadyPaidException;
import cinebox.common.exception.payment.NotFoundPaymentException;
import cinebox.dto.request.PaymentCancelRequest;
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

    // 결제 진행
    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        
    	// 예약 정보 조회
    	Booking booking = bookingRepository.findById(request.getBookingId())
    	        .orElseThrow(() -> NotFoundBookingException.EXCEPTION);

        log.info("예약 ID에 대한 결제 처리 : {}", request.getBookingId());

      
        // 중복 결제 방지: 이미 결제된 예약인지 확인
        if (booking.getStatus() == BookingStatus.PAID) {
            log.warn("이미 결제된 예매입니다. 예매 ID: {}", booking.getBookingId());
            throw AlreadyPaidException.EXCEPTION;
        }

        
        // PaymentMethod enum을 사용하여 결제 방식을 처리
        PaymentMethod method = PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase());
        log.info("Payment method: {}", method);
        // 결제 성공 여부 로그 추가
        log.info("결제 성공 여부 (isPaymentSuccess): {}", request.isPaymentSuccess());

        
        // 프론트에서 전달받은 결제 성공 여부 확인
        PaymentStatus paymentStatus = request.isPaymentSuccess() 
                                      ? PaymentStatus.COMPLETED 
                                      : PaymentStatus.FAILED;
        log.info("Payment status: {}", paymentStatus);
        
        // 결제 정보 저장
        Payment payment = Payment.builder()
                .booking(booking)
                .amount(request.getTotalAmount())
                .method(method)
                .status(paymentStatus)
                .paidAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        log.info("저장된 결제 아이디: {}", payment.getPaymentId());

        
        // 예약 상태 업데이트 (결제 성공 시)
        if (paymentStatus == PaymentStatus.COMPLETED) {
            booking.setStatus(BookingStatus.PAID);
            bookingRepository.save(booking);
        }
        
        
        // PaymentResponse 객체 반환
        return new PaymentResponse(
                booking.getBookingId(),
                payment.getPaymentId(),
                payment.getAmount(),
                method.name(),
                paymentStatus.name(),
                paymentStatus == PaymentStatus.COMPLETED
                ? "결제가 완료되었습니다."
                : "결제가 실패하였습니다."
        );
    }
    



    @Transactional
    public PaymentResponse cancelPayment(PaymentCancelRequest cancelRequest) {
        try {
            Long paymentId = cancelRequest.getPaymentId();
            Long bookingId = cancelRequest.getBookingId();

            // 결제 정보 조회, 결제 정보가 없으면 예외 발생
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> NotFoundPaymentException.EXCEPTION);

            Booking booking = payment.getBooking();

            // 예매 정보가 없거나, 요청된 예약 정보와 일치하지 않으면 예외 발생
            if (booking == null || !booking.getBookingId().equals(bookingId)) {
                throw NotFoundBookingException.EXCEPTION;
            }

            // 결제 상태가 COMPLETED이면 취소 처리
            if (payment.getStatus() == PaymentStatus.COMPLETED) {
                // 예약 상태를 REFUNDED로 변경
                booking.setStatus(BookingStatus.REFUNDED);
                bookingRepository.save(booking);  // 상태 변경 반영

                // 연관된 BookingSeat 삭제
                booking.getBookingSeats().clear();  // 연관된 좌석을 먼저 삭제

                // 예약 삭제
                bookingRepository.delete(booking);  // 예약 삭제

                // 결제 상태를 REFUNDED로 변경
                payment.setStatus(PaymentStatus.REFUNDED);
                paymentRepository.save(payment);

                log.info("예약 삭제 및 결제 취소 완료: bookingId={}, paymentId={}", bookingId, paymentId);
                return new PaymentResponse(bookingId, paymentId, payment.getAmount(),
                        payment.getMethod().toString(), payment.getStatus().toString(), "결제가 취소되었습니다.");
            }

            // 결제 상태가 COMPLETED가 아니면 취소 불가
            return new PaymentResponse(bookingId, paymentId, payment.getAmount(),
                    payment.getMethod().toString(), payment.getStatus().toString(), "결제 취소 불가: 상태 불일치");

        } catch (NotFoundPaymentException | NotFoundBookingException e) {
            // 결제나 예약을 찾지 못한 경우 예외를 로그에 기록하고, 사용자에게 반환
            log.error("예외 발생: ", e);
            throw e;  // 예외를 다시 던져서 처리
        } catch (Exception e) {
            // 예기치 않은 오류 발생 시
            log.error("결제 취소 중 오류 발생: ", e);
            throw new RuntimeException("결제 취소 처리 중 오류가 발생했습니다.");
        }
    }








   
    
    
    
}