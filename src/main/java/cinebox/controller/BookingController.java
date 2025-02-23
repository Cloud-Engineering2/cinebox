package cinebox.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.BookingRequest;
import cinebox.dto.response.BookingResponse;
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
        BookingResponse response = bookingService.bookSeats(request);

        // 예매 성공 여부에 따라 응답 반환
        if ("FAILED".equals(response.getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        } else {
            return ResponseEntity.ok(response);  // 예매 성공 시 response 반환
        }
    }
	

}
	

