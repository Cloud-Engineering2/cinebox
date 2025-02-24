package cinebox.service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.exception.booking.NotFoundScreenException;
import cinebox.common.exception.booking.NotFoundSeatException;
import cinebox.dto.BookingSeatDTO;
import cinebox.dto.request.BookingRequest;
import cinebox.dto.response.BookingResponse;
import cinebox.entity.Booking;
import cinebox.entity.BookingSeat;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import cinebox.repository.BookingRepository;
import cinebox.repository.BookingSeatRepository;
import cinebox.repository.ScreenRepository;
import cinebox.repository.SeatRepository;
import cinebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final ScreenRepository screenRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;


    @Transactional
    public BookingResponse bookSeats(BookingRequest request) {
        // 1. 상영회차 ID로 상영 정보 조회
        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> NotFoundScreenException.EXCEPTION);

        // 상영관 이름 가져오기
        String screenName = screen.getAuditorium().getName();


        
        // 상영관만 기준으로 이미 예약된 좌석 조회, 상영 ID에 해당하는 모든 예약된 좌석을 확인
        List<String> bookedSeats = bookingSeatRepository.findByScreen_ScreenId(
                request.getScreenId()
        ).stream()
                .map(bookingSeat -> bookingSeat.getSeat().getSeatNumber())
                .collect(Collectors.toList());
      
        // 이미 예약된 좌석이 있는지 확인
        if (!bookedSeats.isEmpty()) {
            log.error("이미 예약된 좌석: {}", bookedSeats);

            // 요청된 좌석이 예약된 좌석과 겹치면 예매 실패 처리
            List<String> requestedSeats = request.getSeatNumbers();
            if (requestedSeats.stream().anyMatch(bookedSeats::contains)) {
                
            	// 이미 예약된 좌석 정보를 조회, 요청된 좌석 번호들만 필터링하여 예약된 좌석을 조회
                List<BookingSeat> alreadyBookedSeats = bookingSeatRepository.findByScreen_ScreenIdAndSeat_SeatNumberIn(
                        request.getScreenId(),
                        request.getSeatNumbers()
                );

                // 예약된 상영회차 시간 비교
                List<LocalDateTime> bookedStartTimes = alreadyBookedSeats.stream()
                        .map(bookingSeat -> bookingSeat.getScreen().getStartTime()) // screen의 startTime을 가져옴
                        .distinct() // 중복된 시간은 제거
                        .collect(Collectors.toList());

                if (!bookedStartTimes.isEmpty()) {
                    String errorMessage = "이미 예약된 좌석이 있습니다: " + requestedSeats + " (상영 시간: " + bookedStartTimes + ")";
                    return BookingResponse.builder()
                            .status("FAIL")
                            .message(errorMessage)
                            .screenId(request.getScreenId())
                            .screenName(screenName)
                            .seatNumbers(request.getSeatNumbers())
                            .build();
                }
            }
        }
        
        // 예매 진행 (Booking 및 BookingSeat 객체 생성)
        Booking booking = Booking.builder()
                .bookingDate(LocalDateTime.now())
                .status(BookingStatus.PENDING)
                .user(userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다.")))
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        // 예약된 좌석 저장
        List<BookingSeat> bookingSeats = request.getSeatNumbers().stream()
            .<BookingSeat>map(seatNumber -> {
                // 좌석 조회
                Seat seat = seatRepository.findBySeatNumberAndAuditorium_AuditoriumId(
                        seatNumber,
                        screen.getAuditorium().getAuditoriumId()
                ).orElseThrow(() -> NotFoundSeatException.EXCEPTION);

                // DTO 생성 후 BookingSeat 변환
                BookingSeatDTO bookingSeatDTO = new BookingSeatDTO(null, savedBooking, screen, seat);
                return BookingSeat.fromDTO(bookingSeatDTO);
            })
            .toList();


        bookingSeatRepository.saveAll(bookingSeats);

        // 5. 총 가격 계산
        int totalSeats = request.getSeatNumbers().size();
        BigDecimal totalPrice = screen.getPrice().multiply(new BigDecimal(totalSeats));
        savedBooking.setTotalPrice(totalPrice);

        // 6. 응답 생성
        BookingResponse response = new BookingResponse(
                savedBooking.getBookingId(),
                savedBooking.getBookingDate(),
                request.getScreenId(),
                request.getSeatNumbers(),
                savedBooking.getStatus().toString(),
                totalPrice,
                "예매 성공",
                screenName
        );

        return response;
    }



    
    
}
