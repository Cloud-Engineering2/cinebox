package cinebox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.BookingRequest;
import cinebox.dto.response.BookingResponse;
import cinebox.service.BookingService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
	private final BookingService bookingService;
	
	@PostMapping
	public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
		BookingResponse response = bookingService.createBooking(request);
		return ResponseEntity.ok(response);
	}
}
