package cinebox.service;

import java.util.List;

import cinebox.dto.response.AuditoriumResponse;

public interface AuditoriumService {

	// 상영관 목록 조회
	List<AuditoriumResponse> getAuditoriums();

}
