package cinebox.domain.booking.service;

import java.util.List;

import cinebox.domain.booking.dto.BookingRequest;
import cinebox.domain.booking.dto.BookingResponse;
import cinebox.domain.booking.dto.TicketResponse;
import cinebox.domain.payment.dto.PaymentResponse;

public interface BookingService {
	// 본인의 예매 목록 조회
	List<TicketResponse> getMyBookings();

	// 예매 생성
	BookingResponse createBooking(BookingRequest request);

	// 특정 예매 조회
	TicketResponse getBooking(Long bookingId);

	// 예매 취소 및 환불
	PaymentResponse refundPayment(Long bookingId);

	// 예매 대기 취소
	void cancelBooking(Long bookingId);

	// 특정 사용자의 예매 목록 조회
	List<TicketResponse> getUserBookings(Long userId);
}