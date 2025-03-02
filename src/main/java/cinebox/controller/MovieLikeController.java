package cinebox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.service.MovieLikeService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies/{movieId}/likes")
@RequiredArgsConstructor
public class MovieLikeController {
	private final MovieLikeService movieLikeService;
	
	@PostMapping
	public ResponseEntity<Void> toggleLike(@PathVariable("movieId") Long movieId) {
		movieLikeService.toggleLike(movieId);
		return ResponseEntity.ok().build();
	}
}
