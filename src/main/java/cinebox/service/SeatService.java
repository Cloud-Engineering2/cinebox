package cinebox.service;

import java.util.List;

import cinebox.dto.response.SeatResponse;

public interface SeatService {
	// 좌석 조회
	List<SeatResponse> getSeats(Long screenId);
}
