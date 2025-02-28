package cinebox.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.exception.booking.AlreadyBookedSeatsException;
import cinebox.common.exception.booking.NotFoundBookingException;
import cinebox.common.exception.booking.NotFoundSeatException;
import cinebox.common.exception.screen.NotFoundScreenException;
import cinebox.dto.request.BookingRequest;
import cinebox.dto.response.BookingResponse;
import cinebox.dto.response.TicketResponse;
import cinebox.entity.Booking;
import cinebox.entity.BookingSeat;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import cinebox.entity.User;
import cinebox.repository.BookingRepository;
import cinebox.repository.BookingSeatRepository;
import cinebox.repository.ScreenRepository;
import cinebox.repository.SeatRepository;
import cinebox.security.SecurityUtil;
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

		Screen screen = screenRepository.findById(request.getScreenId())
				.orElseThrow(() -> NotFoundScreenException.EXCEPTION);

		int alreadyBookedCount = bookingSeatRepository.countByScreen_ScreenIdAndSeat_SeatNumberIn(request.getScreenId(), request.getSeatNumbers());
		if (alreadyBookedCount > 0) {
			throw AlreadyBookedSeatsException.EXCEPTION;
		}

		BigDecimal totalPrice = screen.getPrice().multiply(BigDecimal.valueOf(request.getSeatNumbers().size()));

		Booking booking = Booking.createBooking(currentUser, totalPrice);
		Booking savedBooking = bookingRepository.save(booking);

		List<BookingSeat> bookingSeats = request.getSeatNumbers().stream()
				.map(seatNumber -> {
					Seat seat = seatRepository
							.findBySeatNumberAndAuditorium_AuditoriumId(seatNumber, screen.getAuditorium().getAuditoriumId())
							.orElseThrow(() -> NotFoundSeatException.EXCEPTION);
					return new BookingSeat(savedBooking, screen, seat);
				}).collect(Collectors.toList());
		bookingSeatRepository.saveAll(bookingSeats);
		
		return new BookingResponse(savedBooking.getBookingId(), screen.getScreenId());
	}

	// 특정 예매 조회
	@Override
	public TicketResponse getBooking(Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> NotFoundBookingException.EXCEPTION);
		
		return TicketResponse.from(booking);
	}
}
