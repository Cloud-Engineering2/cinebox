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
import cinebox.common.exception.booking.InsufficientBookingStatusException;
import cinebox.common.exception.booking.NotFoundBookingException;
import cinebox.common.exception.booking.NotFoundSeatException;
import cinebox.common.exception.payment.AlreadyRefundedException;
import cinebox.common.exception.payment.NotFoundPaymentException;
import cinebox.common.exception.payment.NotPaidBookingException;
import cinebox.common.exception.screen.NotFoundScreenException;
import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.common.exception.user.NotFoundUserException;
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
import cinebox.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final BookingSeatRepository bookingSeatRepository;
	private final ScreenRepository screenRepository;
	private final SeatRepository seatRepository;
	private final PaymentRepository paymentRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<TicketResponse> getMyBookings() {
		log.info("서비스: 내 예매 정보 조회 시작");
		User currentUser = SecurityUtil.getCurrentUser();
		Long userId = currentUser.getUserId();

		List<Booking> bookings = bookingRepository.findByUser_UserIdAndStatusIn(userId,
				List.of(BookingStatus.PENDING, BookingStatus.PAID));

		List<TicketResponse> responses = bookings.stream()
				.filter(booking -> booking.getBookingSeats() != null && !booking.getBookingSeats().isEmpty())
				.map(TicketResponse::from).collect(Collectors.toList());
		log.info("서비스: 내 예매 정보 조회 완료, 결과 수: {}", responses.size());
		return responses;
	}

	@Override
	@Transactional
	public BookingResponse createBooking(BookingRequest request) {
		log.info("서비스: 예매 생성 시작: screenId={}, seatNumbers={}", request.screenId(), request.seatNumbers());
		User currentUser = SecurityUtil.getCurrentUser();
		
		Integer userAge = currentUser.getAge();
		if (userAge == null) {
			log.error("서비스: 나이 확인 실패, 사용자: {}", currentUser.getUserId());
			throw AgeVerificationException.EXCEPTION;
		}

		Screen screen = screenRepository.findById(request.screenId())
				.orElseThrow(() -> {
					log.error("서비스: 상영 스크린을 찾을 수 없음: screenId={}", request.screenId());
					return NotFoundScreenException.EXCEPTION;
				});
		
		Movie movie = screen.getMovie();
		int requiredAge = movie.getRatingGrade().getMinAge();
		if (userAge < requiredAge) {
			log.error("서비스: 나이 미달: userAge={}, requiredAge={}", userAge, requiredAge);
			throw InsufficientAgeException.EXCEPTION;
		}

		int alreadyBookedCount = bookingSeatRepository.countByScreen_ScreenIdAndSeat_SeatNumberIn(request.screenId(), request.seatNumbers());
		if (alreadyBookedCount > 0) {
			log.error("서비스: 좌석 중복 예매 시도: screenId={}, seatNumbers={}", request.screenId(), request.seatNumbers());
			throw AlreadyBookedSeatsException.EXCEPTION;
		}

		BigDecimal totalPrice = screen.getPrice().multiply(BigDecimal.valueOf(request.seatNumbers().size()));

		Booking booking = Booking.createBooking(currentUser, totalPrice);
		Booking savedBooking = bookingRepository.save(booking);

		List<BookingSeat> bookingSeats = request.seatNumbers().stream()
				.map(seatNumber -> {
					Seat seat = seatRepository
							.findBySeatNumberAndAuditorium_AuditoriumId(seatNumber, screen.getAuditorium().getAuditoriumId())
							.orElseThrow(() -> {
								log.error("서비스: 좌석을 찾을 수 없음: seatNumber={}, auditoriumId={}", seatNumber, screen.getAuditorium().getAuditoriumId());
								return NotFoundSeatException.EXCEPTION;
							});
					return new BookingSeat(savedBooking, screen, seat);
				}).collect(Collectors.toList());
		bookingSeatRepository.saveAll(bookingSeats);
		
		Payment payment = Payment.createPayment(savedBooking);
		paymentRepository.save(payment);
		
		log.info("서비스: 예매 생성 완료: bookingId={}", savedBooking.getBookingId());
		return new BookingResponse(savedBooking.getBookingId(), screen.getScreenId());
	}

	// 특정 사용자의 예매 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<TicketResponse> getUserBookings(Long userId) {
		log.info("서비스: 사용자({}) 예매 정보 조회 시작", userId);
		userRepository.findById(userId).orElseThrow(() -> {
			log.error("서비스: 사용자를 찾을 수 없음: userId={}", userId);
			return NotFoundUserException.EXCEPTION;
		});
		
		List<Booking> bookings = bookingRepository.findByUser_UserIdAndStatusIn(userId,
				List.of(BookingStatus.PENDING, BookingStatus.PAID, BookingStatus.REFUNDED));
		
		List<TicketResponse> responses = bookings.stream()
				.filter(booking -> booking.getBookingSeats() != null && !booking.getBookingSeats().isEmpty())
				.map(TicketResponse::from).collect(Collectors.toList());
		log.info("서비스: 사용자 예매 정보 조회 완료: userId={}, 결과 수: {}", userId, responses.size());
		return responses;
	}

	// 특정 예매 조회
	@Override
	@Transactional(readOnly = true)
	public TicketResponse getBooking(Long bookingId) {
		log.info("서비스: 예매 상세 조회 시작: bookingId={}", bookingId);
		Booking booking = getBookingById(bookingId);
		
		validateUserAuthorization(booking);
		
		log.info("서비스: 예매 상세 조회 성공: bookingId={}", bookingId);
		return TicketResponse.from(booking);
	}

	// 예매 취소 및 환불
	@Override
	@Transactional
	public PaymentResponse refundPayment(Long bookingId) {
		log.info("서비스: 예매 환불 시작: bookingId={}", bookingId);
		Booking booking = getBookingById(bookingId);
		
		validateUserAuthorization(booking);
		
		Payment payment = getLastestPayment(booking);
		
		if (payment.getStatus().equals(PaymentStatus.REFUNDED)
				&& booking.getStatus().equals(BookingStatus.REFUNDED)) {
			log.error("서비스: 이미 환불된 예매: bookingId={}", bookingId);
			throw AlreadyRefundedException.EXCEPTION;
		}
		
		if (!payment.getStatus().equals(PaymentStatus.COMPLETED)
				|| !booking.getStatus().equals(BookingStatus.PAID)) {
			log.error("서비스: 환불 불가능 상태: bookingId={}, paymentStatus={}, bookingStatus={}",
					bookingId, payment.getStatus(), booking.getStatus());
			throw NotPaidBookingException.EXCEPTION;
		}
		
		processRefund(booking, payment);
		log.info("서비스: 예매 환불 완료: bookingId={}", bookingId);
		return PaymentResponse.from(payment);
	}
	
	// 예매 대기 취소
	@Override
	@Transactional
	public void cancelBooking(Long bookingId) {
		log.info("서비스: 예매 대기 취소 시작: bookingId={}", bookingId);
		Booking booking = getBookingById(bookingId);

		validateUserAuthorization(booking);
		
		Payment payment = getLastestPayment(booking);
		
		if (!payment.getStatus().equals(PaymentStatus.REQUESTED)
				|| !booking.getStatus().equals(BookingStatus.PENDING)) {
			log.error("서비스: 취소 불가능한 상태: bookingId={}, paymentStatus={}, bookingStatus={}",
					bookingId, payment.getStatus(), booking.getStatus());
			throw InsufficientBookingStatusException.EXCEPTION;
		}

		processCancel(booking, payment);
		log.info("서비스: 예매 대기 취소 완료: bookingId={}", bookingId);
	}

	private Booking getBookingById(Long bookingId) {
		return bookingRepository.findById(bookingId)
				.orElseThrow(() -> {
					log.error("서비스: 예매를 찾을 수 없음: bookingId={}", bookingId);
					return NotFoundBookingException.EXCEPTION;
				});
	}

	private void validateUserAuthorization(Booking booking) {
		User currentUser = SecurityUtil.getCurrentUser();
		User bookingUser = booking.getUser();

		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(bookingUser.getUserId())) {
			log.error("서비스: 권한 없음, currentUserId={}, bookingUserId={}", currentUser.getUserId(), bookingUser.getUserId());
			throw NoAuthorizedUserException.EXCEPTION;
		}
	}

	private Payment getLastestPayment(Booking booking) {
		return booking.getPayments().stream()
				.max(Comparator.comparing(Payment::getCreatedAt))
				.orElseThrow(() -> {
					log.error("서비스: 결제 정보를 찾을 수 없음: bookingId={}", booking.getBookingId());
					return NotFoundPaymentException.EXCEPTION;
				});
	}

	private void processRefund(Booking booking, Payment payment) {
		log.info("서비스: 환불 처리 시작: bookingId={}", booking.getBookingId());
		booking.getBookingSeats().clear();
		booking.updateStatus(BookingStatus.REFUNDED);
		payment.updateStatus(PaymentStatus.REFUNDED);

		bookingRepository.save(booking);
		paymentRepository.save(payment);
		log.info("서비스: 환불 처리 완료: bookingId={}", booking.getBookingId());
	}

	private void processCancel(Booking booking, Payment payment) {
		log.info("서비스: 예매 취소 처리 시작: bookingId={}", booking.getBookingId());
		booking.getBookingSeats().clear();
		booking.updateStatus(BookingStatus.CANCELED);
		payment.updateStatus(PaymentStatus.FAILED);

		bookingRepository.save(booking);
		paymentRepository.save(payment);
		log.info("서비스: 예매 취소 처리 완료: bookingId={}", booking.getBookingId());
	}
}