package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.ReviewRequest;
import cinebox.dto.response.ReviewResponse;
import cinebox.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies/{movieId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<ReviewResponse> insertReview (@PathVariable(name = "movieId") Long movieId, @RequestBody ReviewRequest reviewRequest) {
		reviewRequest.setMovieId(movieId);
		ReviewResponse newReview = reviewService.insertReview(reviewRequest);
		return ResponseEntity.ok().body(newReview);
	}
	
	@GetMapping
	public ResponseEntity<List<ReviewResponse>> selectReviewByMovieId (@PathVariable(name = "movieId") Long movieId) {
		List<ReviewResponse> reviews = reviewService.selectReviewByMovieId(movieId);
		return ResponseEntity.ok().body(reviews);
	}
	
	@PutMapping("/{reviewId}")
	public ResponseEntity<String> updateReview (@PathVariable(name = "reviewId") Long reviewId, @RequestBody ReviewRequest reviewRequest, HttpServletRequest request) {		
		reviewRequest.setReviewId(reviewId);
		reviewService.updateReview(reviewRequest, request);
		return ResponseEntity.ok().body("Success Update");
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<String> deleteReview (@PathVariable(name = "reviewId") Long reviewId, HttpServletRequest request) {		
		reviewService.deleteReview(reviewId, request);
		return ResponseEntity.ok().body("Success Delete");
	}
}
