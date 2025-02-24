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
import cinebox.dto.request.BookingRequest;
import cinebox.dto.response.BookingResponse;
import cinebox.entity.Booking;
import cinebox.entity.BookingSeat;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import cinebox.repository.BookingRepository;
import cinebox.repository.BookingSeatRepository;
import cinebox.repository.ScreenRepositoryTest;
import cinebox.repository.SeatRepository;
import cinebox.repository.UserRepositoryTest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final ScreenRepositoryTest screenRepository;
    private final UserRepositoryTest userRepository;
    private final SeatRepository seatRepository;

    public BookingService(BookingRepository bookingRepository, BookingSeatRepository bookingSeatRepository,
                          ScreenRepositoryTest screenRepository, UserRepositoryTest userRepository, SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.screenRepository = screenRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public BookingResponse bookSeats(BookingRequest request) {
        // 1. 상영회차 ID로 상영 정보 조회
        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> NotFoundScreenException.EXCEPTION);

        // 상영관 이름 가져오기
        String screenName = screen.getAuditorium().getName();

        // 2. 요청된 상영시간에 예약된 좌석을 확인
        LocalDateTime requestedStartTime = request.getStartTime(); // 사용자가 요청한 상영시간

        // 3. 이미 예약된 좌석 조회 (상영 시간 및 좌석 번호 고려)
        List<String> bookedSeats = bookingSeatRepository.findByScreen_ScreenIdAndSeat_SeatNumberIn(
                request.getScreenId(),
                //requestedStartTime, // 요청된 상영시간
                request.getSeatIds() // 요청된 좌석 번호
        ).stream()
                .map(bookingSeat -> bookingSeat.getSeat().getSeatNumber())
                .collect(Collectors.toList());

      
     // 이미 예약된 좌석이 있는지 확인
        if (!bookedSeats.isEmpty()) {
            log.error("이미 예약된 좌석: {}", bookedSeats);

            // 요청된 좌석이 예약된 좌석과 겹치면 예매 실패 처리
            List<String> requestedSeats = request.getSeatIds();
            if (requestedSeats.stream().anyMatch(bookedSeats::contains)) {
                // 이미 예약된 좌석 정보를 조회
                List<BookingSeat> alreadyBookedSeats = bookingSeatRepository.findByScreen_ScreenIdAndSeat_SeatNumberIn(
                        request.getScreenId(),
                        request.getSeatIds()
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
                            .seatIds(request.getSeatIds())
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
        List<BookingSeat> bookingSeats = request.getSeatIds().stream()
                .map(seatNumber -> {
                    Seat seat = seatRepository.findBySeatNumberAndAuditorium_AuditoriumId(seatNumber, screen.getAuditorium().getAuditoriumId())
                            .orElseThrow(() -> NotFoundSeatException.EXCEPTION);
                    BookingSeat bookingSeat = new BookingSeat();
                    bookingSeat.setScreen(screen);
                    bookingSeat.setBooking(savedBooking);
                    bookingSeat.setSeat(seat);

                    return bookingSeat;
                })
                .collect(Collectors.toList());

        bookingSeatRepository.saveAll(bookingSeats);

        // 5. 총 가격 계산
        int totalSeats = request.getSeatIds().size();
        BigDecimal totalPrice = screen.getPrice().multiply(new BigDecimal(totalSeats));
        savedBooking.setTotalPrice(totalPrice);

        // 6. 응답 생성
        BookingResponse response = new BookingResponse(
                savedBooking.getBookingId(),
                savedBooking.getBookingDate(),
                request.getScreenId(),
                request.getSeatIds(),
                savedBooking.getStatus().toString(),
                totalPrice,
                "예매 성공",
                screenName
        );

        return response;
    }



    
    
}
