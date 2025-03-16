package cinebox.domain.seat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.domain.seat.dto.SeatResponse;
import cinebox.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class SeatController {
	private final SeatService seatService;

	@GetMapping("/{screenId}/seats")
	public ResponseEntity<List<SeatResponse>> getSeats(@PathVariable("screenId") Long screenId) {
		log.info("좌석 조회 요청: screenId={}", screenId);
		List<SeatResponse> responses = seatService.getSeats(screenId);
		log.info("좌석 조회 완료: screenId={}, 좌석 수={}", screenId, responses.size());
		return ResponseEntity.ok(responses);
	}
}
