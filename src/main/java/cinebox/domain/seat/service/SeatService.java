package cinebox.domain.seat.service;

import java.util.List;

import cinebox.domain.seat.dto.SeatResponse;

public interface SeatService {
	// 좌석 조회
	List<SeatResponse> getSeats(Long screenId);
}
