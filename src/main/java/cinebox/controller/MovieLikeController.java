package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.response.MovieResponse;
import cinebox.service.MovieLikeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieLikeController {
	private final MovieLikeService movieLikeService;
	
	@PostMapping("/{movieId}/likes")
	public ResponseEntity<Void> toggleLike(@PathVariable("movieId") Long movieId) {
		movieLikeService.toggleLike(movieId);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("likes")
	public ResponseEntity<List<MovieResponse>> getLikedMovies() {
		List<MovieResponse> responses = movieLikeService.getLikedMovies();
		return ResponseEntity.ok(responses);
	}
}
