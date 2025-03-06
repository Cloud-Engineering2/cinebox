package cinebox.domain.payment.service;

import java.time.LocalDateTime;
import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.enums.PaymentStatus;
import cinebox.common.exception.booking.NotFoundBookingException;
import cinebox.common.exception.payment.AlreadyPaidException;
import cinebox.common.exception.payment.InvalidPaymentStatusException;
import cinebox.common.exception.payment.NotFoundPaymentException;
import cinebox.common.exception.payment.NotPaidBookingException;
import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.domain.booking.entity.Booking;
import cinebox.domain.booking.repository.BookingRepository;
import cinebox.domain.payment.dto.PaymentRequest;
import cinebox.domain.payment.dto.PaymentResponse;
import cinebox.domain.payment.entity.Payment;
import cinebox.domain.payment.repository.PaymentRepository;
import cinebox.domain.user.entity.User;
import cinebox.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;
	private final BookingRepository bookingRepository;

	@Override
	@Transactional
	public PaymentResponse processPayment(PaymentRequest request) {
		Booking booking = bookingRepository.findById(request.bookingId())
				.orElseThrow(() -> NotFoundBookingException.EXCEPTION);

		// 본인만 결제 가능
		User currentUser = SecurityUtil.getCurrentUser();
		User bookingUser = booking.getUser();

		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(bookingUser.getUserId())) {
			throw NoAuthorizedUserException.EXCEPTION;
		}
		
		Payment payment = booking.getPayments().stream()
				.max(Comparator.comparing(Payment::getCreatedAt))
				.orElseThrow(() -> NotFoundPaymentException.EXCEPTION);

		BookingStatus status = booking.getStatus();
		PaymentStatus pStatus = payment.getStatus();
		
		// 중복 결제 방지
		if (!status.equals(BookingStatus.PENDING) 
				|| !pStatus.equals(PaymentStatus.REQUESTED)) {
			if (booking.getStatus().equals(BookingStatus.PAID) 
					&& pStatus.equals(PaymentStatus.COMPLETED)) {
				throw AlreadyPaidException.EXCEPTION;
			} else {
				throw InvalidPaymentStatusException.EXCEPTION;
			}
		}

		payment.processPayment(request);
		paymentRepository.save(payment);
		
		booking.updateStatus(BookingStatus.PAID);
		bookingRepository.save(booking);

		return PaymentResponse.from(payment);
	}

	/**
	 * @deprecated
	 * Booking에서 처리 
	 */
	@Deprecated
	@Override
	public PaymentResponse cancelPayment(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> NotFoundPaymentException.EXCEPTION);
		
		Booking booking = payment.getBooking();
		if (!payment.getStatus().equals(PaymentStatus.COMPLETED)
				|| !booking.getStatus().equals(BookingStatus.PAID)) {
			throw NotPaidBookingException.EXCEPTION;
		}
		
		booking.getBookingSeats().clear();
		booking.updateStatus(BookingStatus.REFUNDED);
		payment.updateStatus(PaymentStatus.REFUNDED);
		
		bookingRepository.save(booking);
		paymentRepository.save(payment);

		return PaymentResponse.from(payment);
	}
}