package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.dto.response.SeatResponse;
import cinebox.dto.response.ShowtimeWithSeatsResponse;
import cinebox.entity.Screen;
import cinebox.repository.BookingSeatRepository;
import cinebox.repository.ScreenRepositoryTest;
import cinebox.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {

    private final ScreenRepositoryTest screenRepository;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;

    /**
     * 특정 영화의 예매 가능한 상영시간과 좌석 조회
     */
    public List<ShowtimeWithSeatsResponse> getShowtimesWithSeats(Long movieId) {
        List<Screen> screens = screenRepository.findByMovie_MovieId(movieId);

        if (screens.isEmpty()) {
            throw NotFoundMovieException.EXCEPTION;  // 예: 영화에 해당하는 상영회차가 없다면 예외 처리
        }
        
        return screens.stream()
                .map(screen -> {
                    // 해당 상영회차의 예매 가능 좌석 조회
                    List<SeatResponse> availableSeats = getAvailableSeatsByScreen(screen.getScreenId());

                    // 응답 DTO 생성
                    return new ShowtimeWithSeatsResponse(
                            screen.getScreenId(),
                            screen.getStartTime(),
                            availableSeats
                    );
                })
                .collect(Collectors.toList());
    }
    
    

    // 특정 상영시간의 예매 가능한 좌석 조회
    public List<SeatResponse> getAvailableSeatsByScreen(Long screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상영 회차를 찾을 수 없습니다: " + screenId));

        List<Long> bookedSeatIds = bookingSeatRepository.findBookedSeatIdsByScreenId(screenId);

        return seatRepository.findByAuditorium(screen.getAuditorium())
                .stream()
                .filter(seat -> !bookedSeatIds.contains(seat.getSeatId()))  // 예매된 좌석 제외
                .map(seat -> new SeatResponse(seat.getSeatId(), seat.getSeatNumber(),seat.getAuditorium().getAuditoriumId(), true))
                .collect(Collectors.toList());
    }
}
