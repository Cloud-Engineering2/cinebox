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


	
	// 등록된시간 노출
//	@GetMapping("/api/movies/{movieId}/showtimes")
//	public ResponseEntity<List<ScreenResponse>> getShowtimes(@PathVariable Long movieId) {
//	    try {
//	        log.info("MovieID: {}", movieId);
//	        List<ScreenResponse> screenResponses = bookingService.getShowtimes(movieId);
//	        return ResponseEntity.ok(screenResponses);
//	    } catch (Exception e) {
//	        log.error("Error occurred while fetching showtimes for movieId {}: {}", movieId, e.getMessage());
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	    }
//	}
	
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
	

