package cinebox.service;

import java.util.List;

import cinebox.dto.request.BookingRequest;
import cinebox.dto.response.BookingResponse;
import cinebox.dto.response.TicketResponse;

public interface BookingService {
	// 본인의 예매 목록 조회
	List<TicketResponse> getMyBookings();

	// 예매 생성
	BookingResponse createBooking(BookingRequest request);
}
