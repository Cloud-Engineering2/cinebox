package cinebox.service;

import java.time.LocalDate;
import java.util.List;

import cinebox.dto.request.ScreenRequest;
import cinebox.dto.response.AuditoriumScreenResponse;
import cinebox.dto.response.DateScreenResponse;
import cinebox.dto.response.ScreenResponse;

public interface ScreenService {
	// 상영 정보 추가
	ScreenResponse createScreen(ScreenRequest request);

	// 상영 정보 수정
	ScreenResponse updateScreen(Long screenId, ScreenRequest request);

	// 상영 정보 삭제
	void deleteScreen(Long screenId);

	// 특정 영화 상영 날짜 목록 조회
	List<LocalDate> getAvailableDatesForMovie(Long movieId);
	
    // 특정 영화의 날짜별 상영 정보 조회
	List<AuditoriumScreenResponse> getScreensByDate(Long movieId, LocalDate date);

	// 모든 상영 정보 조회
	List<DateScreenResponse> getAllScreens();
}
