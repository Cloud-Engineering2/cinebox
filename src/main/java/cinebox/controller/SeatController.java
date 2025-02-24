package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.response.SeatResponse;
import cinebox.service.SeatService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/screens")
@RequiredArgsConstructor
public class SeatController {
	private final SeatService seatService;

	@GetMapping("/{screenId}/seats")
	public ResponseEntity<List<SeatResponse>> getSeats(@PathVariable("screenId") Long screenId) {
		return ResponseEntity.ok(seatService.getSeats(screenId));
	}
}
