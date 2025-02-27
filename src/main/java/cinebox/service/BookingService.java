package cinebox.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.exception.booking.AlreadyBookedSeatsException;
import cinebox.common.exception.booking.NotFoundSeatException;
import cinebox.common.exception.screen.NotFoundScreenException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.dto.BookingSeatDTO;
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
import cinebox.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;
	private final BookingSeatRepository bookingSeatRepository;
	private final ScreenRepository screenRepository;
	private final SeatRepository seatRepository;
	

	public List<BookingResponse> getBookingsByUser() {
	    // 현재 로그인한 사용자 정보 가져오기
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String userName = null;

	    if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails) {
	        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
	        userName = userDetails.getUsername(); // 사용자 이름을 가져옵니다.
	    } else {
	        throw new IllegalStateException("로그인된 사용자가 없습니다.");
	    }

	    // 사용자 이름을 기준으로 예매 목록 조회
	    List<Booking> bookings = bookingRepository.findByUser_Identifier(userName);

	    // 예매 목록을 BookingResponse DTO로 변환하여 반환
	    return bookings.stream().map(booking -> {
	        // 좌석 번호 리스트
	        List<String> seatNumbers = booking.getBookingSeats().stream()
	                .map(bookingSeat -> bookingSeat.getSeat().getSeatNumber()) // 모든 좌석 번호 가져오기
	                .collect(Collectors.toList());

	        // 예매된 좌석이 없으면 기본값 처리
	        if (seatNumbers.isEmpty()) {
	            seatNumbers.add("좌석 없음");
	        }

	        // 첫 번째 예매에 해당하는 스크린 정보
	        BookingSeat firstBookingSeat = booking.getBookingSeats().isEmpty() ? null : booking.getBookingSeats().get(0);
	        Screen screen = (firstBookingSeat != null) ? firstBookingSeat.getScreen() : null;

	        // 결제 정보 처리 (결제 ID 추출)
	        Long paymentId = (booking.getPayments() != null && !booking.getPayments().isEmpty()) 
	            ? booking.getPayments().get(0).getPaymentId() 
	            : null; // 첫 번째 결제 정보에서 paymentId 추출, 없으면 null

	        // 스크린 정보가 없으면 기본값 처리
	        if (screen == null) {
	            screen = new Screen();  // 필요에 따라 예외 처리 또는 기본값 설정
	        }

	        // 총 금액 계산 (좌석 수 × 상영 가격)
	        int totalSeats = booking.getBookingSeats().size();
	        BigDecimal totalPrice = (screen != null && screen.getPrice() != null) 
	                ? screen.getPrice().multiply(BigDecimal.valueOf(totalSeats)) 
	                : BigDecimal.ZERO;  // 가격이 없으면 0으로 설정

	        return new BookingResponse(
	                booking.getBookingId(),
	                paymentId,
	                booking.getBookingDate(),
	                screen.getScreenId(),
	                seatNumbers,
	                booking.getStatus().toString(),
	                totalPrice,
	                screen.getAuditorium() != null ? screen.getAuditorium().getName() : "미정" // 상영관 이름, 없으면 '미정'
	        );
	    }).collect(Collectors.toList());
	}

	

	@Transactional
	public BookingResponse bookSeats(BookingRequest request) {
		// 1. 상영회차 ID로 상영 정보 조회
		Screen screen = screenRepository.findById(request.getScreenId())
				.orElseThrow(() -> NotFoundScreenException.EXCEPTION);

		// 상영관 이름 가져오기
		String screenName = screen.getAuditorium().getName();

		// 2. 이미 예약된 좌석 조회
		List<String> bookedSeats = bookingSeatRepository.findByScreen_ScreenId(request.getScreenId()).stream()
				.map(bookingSeat -> bookingSeat.getSeat().getSeatNumber()).collect(Collectors.toList());

		// 이미 예약된 좌석이 있는지 확인
		List<String> requestedSeats = request.getSeatNumbers();
		if (requestedSeats.stream().anyMatch(bookedSeats::contains)) {
			// 이미 예약된 좌석이 있으면 예외를 던짐
			throw AlreadyBookedSeatsException.EXCEPTION;
		}

		// 현재 로그인한 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();

		// 사용자 정보에서 User 객체 가져오기
		User currentUser = userDetails.getUser();
		if (currentUser == null) {
			throw NotFoundUserException.EXCEPTION;
		}

		// 사용자 이름로그로 출력
		String userName = userDetails.getUsername();
		log.info("현재 로그인한 사용자 이름 : {} ", userName);

		// 예매 진행 (Booking 및 BookingSeat 객체 생성)
		Booking booking = Booking.builder().bookingDate(LocalDateTime.now()).status(BookingStatus.PENDING)
				.user(currentUser) // 로그인한 User 객체 연결
				.build();

		// Booking 저장
		Booking savedBooking = bookingRepository.save(booking);

		// 예약된 좌석 저장
		List<BookingSeat> bookingSeats = request.getSeatNumbers().stream().map(seatNumber -> {
			// 좌석 조회
			Seat seat = seatRepository
					.findBySeatNumberAndAuditorium_AuditoriumId(seatNumber, screen.getAuditorium().getAuditoriumId())
					.orElseThrow(() -> NotFoundSeatException.EXCEPTION);

			// DTO 생성 후 BookingSeat 변환
			BookingSeatDTO bookingSeatDTO = new BookingSeatDTO(null, savedBooking, screen, seat);
			return BookingSeat.fromDTO(bookingSeatDTO);
		}).collect(Collectors.toList());

		// 예약된 좌석 정보 저장
		bookingSeatRepository.saveAll(bookingSeats);

		// 총 가격 계산
		int totalSeats = request.getSeatNumbers().size();
		BigDecimal totalPrice = screen.getPrice().multiply(new BigDecimal(totalSeats));
		savedBooking.setTotalPrice(totalPrice);

		// 결제 정보 처리 (결제 ID 추출)
	    Long paymentId = (savedBooking.getPayments() != null && !savedBooking.getPayments().isEmpty()) 
	        ? savedBooking.getPayments().get(0).getPaymentId() 
	        : null; // 첫 번째 결제 정보에서 paymentId 추출, 없으면 null

	    
		// 예매 상태는 PENDING 상태로 두고 응답을 반환
		BookingResponse bookingResponse = new BookingResponse(
				savedBooking.getBookingId(),
				paymentId,
				savedBooking.getBookingDate(), request.getScreenId(), request.getSeatNumbers(),
				savedBooking.getStatus().toString(), totalPrice, "예매 성공", screenName);

		return bookingResponse;
	}

}