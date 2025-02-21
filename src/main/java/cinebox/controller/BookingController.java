package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import cinebox.dto.response.SeatResponse;
import cinebox.dto.response.ShowtimeWithSeatsResponse;
import cinebox.service.BookingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

		private final BookingService bookingService;


        // 선택한 시간대의 좌석 조회
        @GetMapping("/showtimes/{screenId}/available-seats")
        public ResponseEntity<List<SeatResponse>> getAvailableSeatsByScreen(@PathVariable Long screenId) {
            List<SeatResponse> availableSeats = bookingService.getAvailableSeatsByScreen(screenId);
            return ResponseEntity.ok(availableSeats);
        }
        
        /*  특정 영화의 예매 가능한 상영시간과 좌석 조회 (한번에 조회)
         */
        @GetMapping("/movie/{movieId}/available-showtimes-and-seats")
        public ResponseEntity<List<ShowtimeWithSeatsResponse>> getShowtimesAndSeats(@PathVariable Long movieId) {
            List<ShowtimeWithSeatsResponse> response = bookingService.getShowtimesWithSeats(movieId);
            return ResponseEntity.ok(response);
        }
        
    }
