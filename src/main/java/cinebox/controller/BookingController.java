package cinebox.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.common.exception.booking.AlreadyBookedSeatsException;
import cinebox.dto.request.BookingRequest;
import cinebox.dto.response.BookingResponse;
import cinebox.service.BookingService;
import cinebox.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
	
	private final BookingService bookingService;
	private final PaymentService paymentService;
		

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
	    	 log.error("예기치 않은 오류 발생: ", ex);  // 예외 메시지와 스택 트레이스를 로그에 기록
	    	    
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예기치 않은 오류가 발생했습니다.");
	    }
	}
	
    
	
}
	

