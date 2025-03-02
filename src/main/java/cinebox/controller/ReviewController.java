package cinebox.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.ReviewRequest;
import cinebox.dto.response.ReviewResponse;
import cinebox.dto.validation.CreateGroup;
import cinebox.dto.validation.UpdateGroup;
import cinebox.service.ReviewService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;
	
	// 리뷰 생성
	@PostMapping("/movies/{movieId}/reviews")
	public ResponseEntity<ReviewResponse> createReview(
			@PathVariable("movieId") Long movieId,
			@Validated(CreateGroup.class) @RequestBody ReviewRequest request) {
		ReviewResponse response = reviewService.createReview(movieId, request);
		return ResponseEntity.ok(response);
	}
	
	// 리뷰 수정
	@PutMapping("/reivews/{reviewId}")
	public ResponseEntity<ReviewResponse> updateReview(
			@PathVariable("reviewId") Long reviewId,
			@Validated(UpdateGroup.class) @RequestBody ReviewRequest request) {
		ReviewResponse response = reviewService.updateReview(reviewId, request);
		return ResponseEntity.ok(response);
	}
	
	// 특정 영화의 리뷰 목록 조회
	@GetMapping("/movies/{movieId}/reviews")
	public ResponseEntity<List<ReviewResponse>> getReviewsByMovieId(
			@PathVariable("movieId") Long movieId) {
		List<ReviewResponse> responses = reviewService.getReviewsByMovieId(movieId);
		return ResponseEntity.ok(responses);
	}
	
	// 리뷰 삭제
	@GetMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> deleteReviewById(
			@PathVariable("reviewId") Long reviewId) {
		reviewService.deleteReview(reviewId);
		return ResponseEntity.noContent().build();
	}
	
	// 본인 리뷰 목록 조회
	@GetMapping("/reviews/my")
	public ResponseEntity<List<ReviewResponse>> getMyReviews() {
		List<ReviewResponse> responses = reviewService.getMyReviews();
		return ResponseEntity.ok(responses);
	}
	
	// 특정 유저의 리뷰 목록 조회
	@GetMapping("/users/{userId}/reviews")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<ReviewResponse>> getReviewsByUser(
			@PathVariable("userId") Long userId) {
		List<ReviewResponse> responses = reviewService.getReviewsByUser(userId);
		return ResponseEntity.ok(responses);
	}
}
