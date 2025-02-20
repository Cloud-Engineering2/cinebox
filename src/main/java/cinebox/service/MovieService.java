package cinebox.service;

import java.util.List;

import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;

public interface MovieService {
	// create
	// 영화 등록(생성)
	MovieResponse registerMovie(MovieRequest request);
	
	// read
	// 영화 목록 조회 (정렬, 검색)
	List<MovieResponse> getAllMovies(String sortBy, String searchText);
	
	// 특정 영화 조회
	MovieResponse getMovie(Long movie_id);
	
	// update
	// 영화 정보 수정
	MovieResponse updateMovie(Long movie_id, MovieRequest request);

	// delete
	// 영화 삭제
	void deleteMovie(Long movie_id);
}
