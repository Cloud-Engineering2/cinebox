package cinebox.domain.auditorium.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.common.validation.CreateGroup;
import cinebox.common.validation.UpdateGroup;
import cinebox.domain.auditorium.dto.AuditoriumRequest;
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

	// 상영관 생성
	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<AuditoriumResponse> createAuditorium(@RequestBody @Validated(CreateGroup.class) AuditoriumRequest request) {
		AuditoriumResponse response = auditoriumService.createAuditorium(request);
		return ResponseEntity.ok(response);
	}

	// 상영관 이름 수정
	@PutMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<AuditoriumResponse> updateAuditorium(
			@PathVariable("auditoriumId") Long auditoriumId,
			@RequestBody @Validated(UpdateGroup.class) AuditoriumRequest request) {
		AuditoriumResponse response = auditoriumService.updateAuditorium(auditoriumId, request);
		return ResponseEntity.ok(response);
	}
}
