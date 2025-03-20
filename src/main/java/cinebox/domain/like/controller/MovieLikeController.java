package cinebox.domain.like.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.domain.like.service.MovieLikeService;
import cinebox.domain.movie.dto.MovieResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieLikeController {
	private final MovieLikeService movieLikeService;
	
	@PostMapping("/{movieId}/likes")
	public ResponseEntity<Void> toggleLike(@PathVariable("movieId") Long movieId) {
		log.info("영화 좋아요 토글 요청: movieId={}", movieId);
		movieLikeService.toggleLike(movieId);
		log.info("영화 좋아요 토글 완료: movieId={}", movieId);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("likes")
	public ResponseEntity<List<MovieResponse>> getLikedMovies() {
		log.info("내 좋아요 영화 목록 조회 요청");
		List<MovieResponse> responses = movieLikeService.getLikedMovies();
		log.info("내 좋아요 영화 목록 조회 완료, 결과 수: {}", responses.size());
		return ResponseEntity.ok(responses);
	}
}
