package cinebox.service;

import cinebox.dto.request.ScreenRequest;
import cinebox.dto.response.ScreenResponse;

public interface ScreenService {
	// 상영 정보 추가
	ScreenResponse createScreen(ScreenRequest request);

	// 상영 정보 수정
	ScreenResponse updateScreen(Long screenId, ScreenRequest request);

	// 상영 정보 삭제
	void deleteScreen(Long screenId);
}
