package cinebox.service;

import java.time.LocalDate;
import java.util.List;

import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;
import cinebox.dto.response.ScreenResponse;

public interface MovieService {
	// create
	// 영화 등록(생성)
	MovieResponse registerMovie(MovieRequest request);
	
	// read
	// 영화 목록 조회 (정렬, 검색)
	List<MovieResponse> getAllMovies(String sortBy, String searchText);
	
	// 특정 영화 조회
	MovieResponse getMovie(Long movieId);

	// 특정 영화 상영 날짜 목록 조회
	List<LocalDate> getAvailableDatesForMovie(Long movieId);
	
    // 특정 영화의 날짜별 상영 정보 조회
	List<ScreenResponse> getScreensByDate(Long movieId, LocalDate date);
	
	// update
	// 영화 정보 수정
	MovieResponse updateMovie(Long movieId, MovieRequest request);

	// delete
	// 영화 삭제
	void deleteMovie(Long movieId);
}
