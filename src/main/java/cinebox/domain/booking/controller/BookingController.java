package cinebox.domain.booking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.domain.booking.dto.BookingRequest;
import cinebox.domain.booking.dto.BookingResponse;
import cinebox.domain.booking.dto.TicketResponse;
import cinebox.domain.booking.service.BookingService;
import cinebox.domain.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BookingController {

	private final BookingService bookingService;

	// 예매 생성
	@PostMapping("/bookings")
	public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
		log.info("예매 생성 요청: screenId={}, seatNumbers={}", request.screenId(), request.seatNumbers());
		BookingResponse response = bookingService.createBooking(request);
		log.info("예매 생성 성공: bookingId={}", response.bookingId());
		return ResponseEntity.ok(response);
	}

	// 본인의 예매 정보 조회
	@GetMapping("/bookings/my")
	public ResponseEntity<List<TicketResponse>> getMyBookings() {
		log.info("내 예매 정보 조회 요청");
		List<TicketResponse> responses = bookingService.getMyBookings();
		log.info("내 예매 정보 조회 성공, 결과 수: {}", responses.size());
		return ResponseEntity.ok(responses);
	}

	// 특정 사용자의 예매 목록 조회
	@GetMapping("/users/{userId}/bookings")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<TicketResponse>> getUserBookings(@PathVariable("userId") Long userId) {
		log.info("관리자: 사용자({}) 예매 목록 조회 요청", userId);
		List<TicketResponse> responses = bookingService.getUserBookings(userId);
		log.info("관리자: 사용자 예매 목록 조회 성공, 결과 수: {}", responses.size());
		return ResponseEntity.ok(responses);
	}

	// 특정 예매 조회
	@GetMapping("/bookings/{bookingId}")
	public ResponseEntity<TicketResponse> getBooking(@PathVariable("bookingId") Long bookingId) {
		log.info("예매 상세 조회 요청: bookingId={}", bookingId);
		TicketResponse response = bookingService.getBooking(bookingId);
		log.info("예매 상세 조회 성공: bookingId={}", bookingId);
		return ResponseEntity.ok(response);
	}

	// 예매 취소 및 환불
	@PostMapping("/bookings/{bookingId}")
	public ResponseEntity<PaymentResponse> refundBooking(@PathVariable("bookingId") Long bookingId) {
		log.info("예매 환불 요청: bookingId={}", bookingId);
		PaymentResponse paymentResponse = bookingService.refundPayment(bookingId);
		log.info("예매 환불 성공: bookingId={}", bookingId);
		return ResponseEntity.ok(paymentResponse);
	}

	// 예매 대기 취소
	@PostMapping("/bookings/{bookingId}/cancel")
	public ResponseEntity<Void> cancelBooking(@PathVariable("bookingId") Long bookingId) {
		log.info("예매 대기 취소 요청: bookingId={}", bookingId);
		bookingService.cancelBooking(bookingId);
		log.info("예매 대기 취소 성공: bookingId={}", bookingId);
		return ResponseEntity.noContent().build();
	}
}