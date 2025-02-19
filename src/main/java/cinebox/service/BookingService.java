package cinebox.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cinebox.dto.response.ScreenResponseTest;
import cinebox.dto.response.SeatResponseDTO;
import cinebox.entity.BookingSeat;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import cinebox.repository.BookingSeatRepository;
import cinebox.repository.ScreenRepositoryTest;
import cinebox.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final ScreenRepositoryTest screenRepositoryTest;  // 상영 정보 조회
    private final BookingSeatRepository bookingSeatRepository;  // 예약된 좌석 정보 조회
    private final SeatRepository seatRepository;  // 좌석 정보 조회 (예시)

    
    public List<ScreenResponseTest> findAvailableShowtimes(Long movieId) {
        List<Screen> screens = screenRepositoryTest.findByMovie_MovieId(movieId);
        List<ScreenResponseTest> response = new ArrayList<>();
        log.info("영화 ID: {}에 대한 사용 가능한 상영시간을 조회 중입니다.", movieId);

        for (Screen screen : screens) {
            log.info("Processing screen with ID: {}", screen.getScreenId());

            // 1. 해당 상영의 모든 좌석 조회
            List<Seat> allSeats = seatRepository.findByAuditorium_AuditoriumId(screen.getAuditorium().getAuditoriumId());
            log.info("상영 ID: {}에 대한 모든 좌석 조회 결과: {}", screen.getScreenId(), allSeats.size());

            // 2. 해당 상영의 예매된 좌석 조회
            List<BookingSeat> bookedSeats = bookingSeatRepository.findByScreen(screen);  // 예매된 좌석 조회
            log.info("상영 ID: {}에 대해 예매된 좌석 조회 결과: {}", screen.getScreenId(), bookedSeats.size());

            // 3. 예매되지 않은 좌석만 필터
            List<SeatResponseDTO> availableSeats = allSeats.stream()
                .filter(seat -> {
                    boolean isBooked = bookedSeats.stream()
                        .anyMatch(bookingSeat -> bookingSeat.getSeat().getSeatId().equals(seat.getSeatId()));
                    if (isBooked) {
                        log.debug("예매된 좌석: {} (좌석 ID: {})", seat.getSeatNumber(), seat.getSeatId());
                    } else {
                        log.debug("사용 가능한 좌석: {} (좌석 ID: {})", seat.getSeatNumber(), seat.getSeatId());
                    }
                    return !isBooked;  // 예매되지 않은 좌석만 필터링
                })
                .map(seat -> new SeatResponseDTO(seat.getSeatId(), seat.getSeatNumber())) // SeatResponseDTO로 변환
                .collect(Collectors.toList());  // 예매되지 않은 좌석 목록 생성

            // 4. 응답 객체 생성
            ScreenResponseTest screenResponse = new ScreenResponseTest(
                screen.getScreenId(),
                screen.getStartTime(),
                screen.getEndTime(),
                screen.getPrice(),
                availableSeats
            );
            response.add(screenResponse);
            log.info("상영 ID: {} 처리 완료", screen.getScreenId());
        }

        log.info("사용 가능한 상영시간 조회 결과: {}개", response.size());
        return response;
    }


}

