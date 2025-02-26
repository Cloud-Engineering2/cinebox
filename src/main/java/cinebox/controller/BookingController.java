package cinebox.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.common.exception.booking.AlreadyBookedSeatsException;
import cinebox.dto.request.BookingRequest;
import cinebox.dto.response.BookingResponse;
import cinebox.dto.response.TicketResponse;
import cinebox.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
	
	private final BookingService bookingService;
		

	@PostMapping
	public ResponseEntity<?> bookSeats(@RequestBody BookingRequest request) {
	    try {
	        BookingResponse response = bookingService.bookSeats(request);
	        return ResponseEntity.ok(response);  // 예매 성공 시 response 반환
	    } catch (AlreadyBookedSeatsException ex) {
	        // 이미 예약된 좌석이 있을 경우
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	    } catch (Exception ex) {
	        // 다른 예외 처리 (필요 시)
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예기치 않은 오류가 발생했습니다.");
	    }
	}
	
	// 본인의 예매 정보 조회
	// Angela 작업과 병합 후 BookingResponse와 병합 고려
	@GetMapping
	public ResponseEntity<List<TicketResponse>> getMyBookings() {
		List<TicketResponse> responses = bookingService.getMyBookings();
		return ResponseEntity.ok(responses);
	}
}
	

