package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public ResponseEntity<List<MovieResponse>> getAllMovies() {
		return ResponseEntity.ok(movieService.getAllMovies());
	}
}
