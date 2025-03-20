package cinebox.domain.movie.service;

import java.util.List;

import cinebox.domain.movie.dto.MovieRequest;
import cinebox.domain.movie.dto.MovieResponse;

public interface MovieService {
	// create
	// 영화 등록(생성)
	MovieResponse registerMovie(MovieRequest request, String posterImageUrl);
	
	// read
	// 영화 목록 조회 (정렬, 검색)
	List<MovieResponse> getAllMovies(String sortBy, String searchText);
	
	// 특정 영화 조회
	MovieResponse getMovie(Long movieId);
	
	// update
	// 영화 정보 수정
	MovieResponse updateMovie(Long movieId, MovieRequest request, String posterImageUrl);

	// delete
	// 영화 삭제
	void deleteMovie(Long movieId);
}
