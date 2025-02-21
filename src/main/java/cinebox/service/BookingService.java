package cinebox.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.dto.request.BookingRequest;
import cinebox.dto.response.BookingResponse;
import cinebox.entity.Booking;
import cinebox.entity.BookingSeat;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import cinebox.entity.User;
import cinebox.repository.BookingRepository;
import cinebox.repository.BookingSeatRepository;
import cinebox.repository.ScreenRepository;
import cinebox.repository.SeatRepository;
import cinebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {
	private final BookingRepository bookingRepository;
	private final BookingSeatRepository bookingSeatRepository;
	private final ScreenRepository screenRepository;
	private final SeatRepository seatRepository;
	private final UserRepository userRepository;	// 임시
	
	@Transactional
	public BookingResponse createBooking(BookingRequest bookingRequest) {
		User user = userRepository.findById(1L)
				.orElseThrow(() -> NotFoundUserException.EXCEPTION);	// 임시
				
        Screen screen = screenRepository.findById(bookingRequest.screenId())
            .orElseThrow(() -> new RuntimeException("Screen not found"));

        // 선택된 좌석들에 대한 검증 생략 (해당 상영관의 좌석인지, 이미 예약되지 않았는지 등)
        List<Seat> seats = seatRepository.findAllById(bookingRequest.seatIds());

        Booking booking = Booking.builder()
            .user(user)
            .bookingDate(LocalDateTime.now())
            .totalPrice(bookingRequest.totalPrice())
            .status(BookingStatus.PENDING)
            .build();
        bookingRepository.save(booking);

        // 각 좌석에 대해 BookingSeat 생성
        List<BookingSeat> bookingSeats = seats.stream()
            .map(seat -> {
                BookingSeat bs = new BookingSeat();
                bs.setBooking(booking);
                bs.setScreen(screen);
                bs.setSeat(seat);
                return bs;
            })
            .collect(Collectors.toList());
        bookingSeatRepository.saveAll(bookingSeats);

        BookingResponse response = new BookingResponse(
            booking.getBookingId(),
            booking.getBookingDate(),
            screen.getScreenId(),
            bookingRequest.seatIds(),
            booking.getStatus().name()
        );
        return response;
    }
}
