package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	// 예매 생성
	@PostMapping
	public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
		BookingResponse response = bookingService.createBooking(request);
		return ResponseEntity.ok(response);
	}

	// 본인의 예매 정보 조회
	@GetMapping("/my")
	public ResponseEntity<List<TicketResponse>> getMyBookings() {
		List<TicketResponse> responses = bookingService.getMyBookings();
		return ResponseEntity.ok(responses);
	}
}
