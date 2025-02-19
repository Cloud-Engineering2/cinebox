package cinebox.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cinebox.dto.ScreenResponseTest;
import cinebox.dto.SeatDTO;
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
        log.info("영화 ID: {}에 대한 사용 가능한 상영시간을 조회 중입니다.", movieId);  // log.info로 로그 추가

        for (Screen screen : screens) {
            log.info("Processing screen with ID: {}", screen.getScreenId());  // 각 상영에 대한 info 로그

            // 1. 해당 상영의 모든 좌석 조회
            List<Seat> allSeats = seatRepository.findByScreen_ScreenId(screen.getScreenId());
            log.info("상영 ID: {}에 대한 모든 좌석 조회 결과: {}", screen.getScreenId(), allSeats.size());
            log.debug("상영 ID: {}에 대한 좌석 목록: {}", screen.getScreenId(), allSeats);  // 좌석 목록 로그 추가

            // 2. 해당 상영의 예매된 좌석 조회
            List<BookingSeat> bookedSeats = bookingSeatRepository.findByScreen(screen);  // 수정된 부분
            log.info("상영 ID: {}에 대해 예매된 좌석 조회 결과: {}", screen.getScreenId(), bookedSeats.size());
            log.debug("상영 ID: {}에 대한 예매된 좌석 목록: {}", screen.getScreenId(), bookedSeats);  // 예매된 좌석 목록 로그 추가

            // 3. 예매되지 않은 좌석만 필터링
            List<SeatDTO> availableSeats = allSeats.stream()
            	    .filter(seat -> {
            	        boolean isBooked = bookedSeats.stream()
            	            .anyMatch(bookingSeat -> bookingSeat.getSeat().getSeatId().equals(seat.getSeatId()));
            	        if (isBooked) {
            	            log.debug("예매된 좌석: {} (좌석 ID: {})", seat.getSeatNumber(), seat.getSeatId());
            	        } else {
            	            log.debug("사용 가능한 좌석: {} (좌석 ID: {})", seat.getSeatNumber(), seat.getSeatId());
            	        }
            	        return !isBooked;
            	    })
            	    .map(seat -> new SeatDTO(seat.getSeatId(), seat.getSeatNumber()))
            	    .collect(Collectors.toList());
            // 4. 응답 객체 생성
            ScreenResponseTest screenResponse = new ScreenResponseTest(
                    screen.getScreenId(),
                    screen.getStartTime(),
                    screen.getEndTime(),
                    screen.getPrice(),
                    availableSeats  
            );
            response.add(screenResponse);
            log.info("상영 ID: {} 처리 완료", screen.getScreenId());  // 각 상영 처리 완료 로그
        }

        log.info("사용 가능한 상영시간 조회 결과: {}개", response.size());  // 최종 결과 로그
        return response;
    }

}

