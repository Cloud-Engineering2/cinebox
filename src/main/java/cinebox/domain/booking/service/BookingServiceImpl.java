package cinebox.domain.booking.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.enums.PaymentStatus;
import cinebox.common.exception.booking.AgeVerificationException;
import cinebox.common.exception.booking.AlreadyBookedSeatsException;
import cinebox.common.exception.booking.InsufficientAgeException;
import cinebox.common.exception.booking.NotFoundBookingException;
import cinebox.common.exception.booking.NotFoundSeatException;
import cinebox.common.exception.payment.AlreadyRefundedException;
import cinebox.common.exception.payment.NotFoundPaymentException;
import cinebox.common.exception.payment.NotPaidBookingException;
import cinebox.common.exception.screen.NotFoundScreenException;
import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.common.utils.SecurityUtil;
import cinebox.domain.booking.dto.BookingRequest;
import cinebox.domain.booking.dto.BookingResponse;
import cinebox.domain.booking.dto.TicketResponse;
import cinebox.domain.booking.entity.Booking;
import cinebox.domain.booking.entity.BookingSeat;
import cinebox.domain.booking.repository.BookingRepository;
import cinebox.domain.booking.repository.BookingSeatRepository;
import cinebox.domain.movie.entity.Movie;
import cinebox.domain.payment.dto.PaymentResponse;
import cinebox.domain.payment.entity.Payment;
import cinebox.domain.payment.repository.PaymentRepository;
import cinebox.domain.screen.entity.Screen;
import cinebox.domain.screen.repository.ScreenRepository;
import cinebox.domain.seat.entity.Seat;
import cinebox.domain.seat.repository.SeatRepository;
import cinebox.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final BookingSeatRepository bookingSeatRepository;
	private final ScreenRepository screenRepository;
	private final SeatRepository seatRepository;
	private final PaymentRepository paymentRepository;

	@Override
	@Transactional(readOnly = true)
	public List<TicketResponse> getMyBookings() {
		User currentUser = SecurityUtil.getCurrentUser();
		Long userId = currentUser.getUserId();

		List<Booking> bookings = bookingRepository.findByUser_UserIdAndStatusIn(userId,
				List.of(BookingStatus.PENDING, BookingStatus.PAID));

		return bookings.stream()
				.filter(booking -> booking.getBookingSeats() != null && !booking.getBookingSeats().isEmpty())
				.map(TicketResponse::from).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public BookingResponse createBooking(BookingRequest request) {
		User currentUser = SecurityUtil.getCurrentUser();
		
		Integer userAge = currentUser.getAge();
		if (userAge == null) {
			throw AgeVerificationException.EXCEPTION;
		}

		Screen screen = screenRepository.findById(request.screenId())
				.orElseThrow(() -> NotFoundScreenException.EXCEPTION);
		
		Movie movie = screen.getMovie();
		int requiredAge = movie.getRatingGrade().getMinAge();
		if (userAge < requiredAge) {
			throw InsufficientAgeException.EXCEPTION;
		}

		int alreadyBookedCount = bookingSeatRepository.countByScreen_ScreenIdAndSeat_SeatNumberIn(request.screenId(), request.seatNumbers());
		if (alreadyBookedCount > 0) {
			throw AlreadyBookedSeatsException.EXCEPTION;
		}

		BigDecimal totalPrice = screen.getPrice().multiply(BigDecimal.valueOf(request.seatNumbers().size()));

		Booking booking = Booking.createBooking(currentUser, totalPrice);
		Booking savedBooking = bookingRepository.save(booking);

		List<BookingSeat> bookingSeats = request.seatNumbers().stream()
				.map(seatNumber -> {
					Seat seat = seatRepository
							.findBySeatNumberAndAuditorium_AuditoriumId(seatNumber, screen.getAuditorium().getAuditoriumId())
							.orElseThrow(() -> NotFoundSeatException.EXCEPTION);
					return new BookingSeat(savedBooking, screen, seat);
				}).collect(Collectors.toList());
		bookingSeatRepository.saveAll(bookingSeats);
		
		Payment payment = Payment.createPayment(savedBooking);
		paymentRepository.save(payment);
		
		return new BookingResponse(savedBooking.getBookingId(), screen.getScreenId());
	}

	// 특정 예매 조회
	@Override
	public TicketResponse getBooking(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> NotFoundBookingException.EXCEPTION);
		
		User currentUser = SecurityUtil.getCurrentUser();
		User bookingUser = booking.getUser();
		
		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(bookingUser.getUserId())) {
			throw NoAuthorizedUserException.EXCEPTION;
		}
		
		return TicketResponse.from(booking);
	}

	// 예매 취소 및 환불
	@Override
	@Transactional
	public PaymentResponse refundPayment(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> NotFoundBookingException.EXCEPTION);
		
		User currentUser = SecurityUtil.getCurrentUser();
		User bookingUser = booking.getUser();
		
		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(bookingUser.getUserId())) {
			throw NoAuthorizedUserException.EXCEPTION;
		}
		
		Payment payment = booking.getPayments().stream()
				.max(Comparator.comparing(Payment::getCreatedAt))
				.orElseThrow(() -> NotFoundPaymentException.EXCEPTION);
		
		if (payment.getStatus().equals(PaymentStatus.REFUNDED)
				&& booking.getStatus().equals(BookingStatus.REFUNDED)) {
			throw AlreadyRefundedException.EXCEPTION;
		}
		
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
