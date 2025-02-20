package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;
import cinebox.service.MovieService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
	private final MovieService movieService;

	// TODO: s3 연동 및 MultiPart 적용
	// 영화 등록
	@PostMapping
	public ResponseEntity<MovieResponse> registerMovie(@RequestBody MovieRequest request) {
		return ResponseEntity.ok(movieService.registerMovie(request));
	}
	
	// TODO: 사용자 권한에 따른 조회 범위 구분 (ADMIN을 제외한 권한은 UPCOMING과 SHOWING 만 조회)
	// 모든 영화 조회
	@GetMapping
	public ResponseEntity<List<MovieResponse>> getAllMovies(
			@RequestParam(name = "sort", required = false) String sortBy,
			@RequestParam(name = "search", required = false) String searchText) {
		return ResponseEntity.ok(movieService.getAllMovies(sortBy, searchText));
	}
	
	//TODO: 사용자 권한에 따른 조회 범위 구분 (ADMIN을 제외한 권한은 UNRELEASED에 대한 조회 불가
	@GetMapping("/{movie_id}")
	public ResponseEntity<MovieResponse> getMovie(@PathVariable("movie_id") Long movie_id) {
		return ResponseEntity.ok(movieService.getMovie(movie_id));
	}
	
	//TODO: 사용자 권한에 따른 접근 제한 + s3 연동하여 이미지 처리
	@PutMapping("/{movie_id}")
	public ResponseEntity<MovieResponse> updateMovie(
			@PathVariable("movie_id") Long movie_id,
			@RequestBody MovieRequest request) {
		return ResponseEntity.ok(movieService.updateMovie(movie_id, request));
	}
}
