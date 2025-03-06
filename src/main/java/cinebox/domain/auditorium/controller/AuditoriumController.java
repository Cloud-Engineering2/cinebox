package cinebox.domain.auditorium.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.domain.auditorium.dto.AuditoriumResponse;
import cinebox.domain.auditorium.service.AuditoriumService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auditoriums")
@RequiredArgsConstructor
public class AuditoriumController {

	private final AuditoriumService auditoriumService;

	// 상영관 목록 조회
	@GetMapping
	public ResponseEntity<List<AuditoriumResponse>> getAuditoriums() {
		List<AuditoriumResponse> responses = auditoriumService.getAuditoriums();
		return ResponseEntity.ok(responses);
	}

}
