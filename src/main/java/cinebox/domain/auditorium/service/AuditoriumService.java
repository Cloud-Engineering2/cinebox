package cinebox.domain.auditorium.service;

import java.util.List;

import cinebox.domain.auditorium.dto.AuditoriumRequest;
import cinebox.domain.auditorium.dto.AuditoriumResponse;

public interface AuditoriumService {

	// 상영관 목록 조회
	List<AuditoriumResponse> getAuditoriums();

	// 상영관 생성
	AuditoriumResponse createAuditorium(AuditoriumRequest request);

}
