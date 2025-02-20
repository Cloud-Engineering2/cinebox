package cinebox.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.response.ScreenResponseTest;
import cinebox.dto.response.SeatResponseDTO;
import cinebox.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/movies")
@Slf4j
public class BookingController {

	private final BookingService bookingService;

	
	// 예매 가능한 상영시간 조회
	@GetMapping("/{movieId}/showtimes")
	public List<ScreenResponseTest> getAvailableShowtimes(@PathVariable Long movieId) {
	    return bookingService.findAvailableShowtimes(movieId);
	}
	
	// 좌석 선택 (특정 상영의 예매 가능한 좌석 조회)
    @GetMapping("/{movieId}/showtimes/{screenId}/seats")
    public List<SeatResponseDTO> getAvailableSeats(@PathVariable Long screenId) {
        return bookingService.selectSeatsForShowtime(screenId);
    }

	

}
	

