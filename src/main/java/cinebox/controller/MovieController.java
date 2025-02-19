package cinebox.controller;

import org.springframework.http.ResponseEntity;
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

	// To-Do: s3 연동 및 MultiPart 적용
	// 영화 등록
	@PostMapping
	public ResponseEntity<MovieResponse> registerMovie(@RequestBody MovieRequest request) {
		return ResponseEntity.ok(movieService.registerMovie(request));
	}
}
