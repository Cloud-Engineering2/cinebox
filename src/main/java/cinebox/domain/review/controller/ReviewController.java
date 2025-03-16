package cinebox.domain.review.controller;

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

import cinebox.common.validation.CreateGroup;
import cinebox.common.validation.UpdateGroup;
import cinebox.domain.review.dto.ReviewRequest;
import cinebox.domain.review.dto.ReviewResponse;
import cinebox.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.info("리뷰 생성 요청: movieId={}, 내용={}", movieId, request.content());
		ReviewResponse response = reviewService.createReview(movieId, request);
		log.info("리뷰 생성 완료: reviewId={}", response.reviewId());
		return ResponseEntity.ok(response);
	}
	
	// 리뷰 수정
	@PutMapping("/reviews/{reviewId}")
	public ResponseEntity<ReviewResponse> updateReview(
			@PathVariable("reviewId") Long reviewId,
			@Validated(UpdateGroup.class) @RequestBody ReviewRequest request) {
		log.info("리뷰 수정 요청: reviewId={}", reviewId);
		ReviewResponse response = reviewService.updateReview(reviewId, request);
		log.info("리뷰 수정 완료: reviewId={}", reviewId);
		return ResponseEntity.ok(response);
	}
	
	// 특정 영화의 리뷰 목록 조회
	@GetMapping("/movies/{movieId}/reviews")
	public ResponseEntity<List<ReviewResponse>> getReviewsByMovieId(
			@PathVariable("movieId") Long movieId) {
		log.info("영화 리뷰 목록 조회 요청: movieId={}", movieId);
		List<ReviewResponse> responses = reviewService.getReviewsByMovieId(movieId);
		log.info("영화 리뷰 목록 조회 완료: movieId={}, 결과 수={}", movieId, responses.size());
		return ResponseEntity.ok(responses);
	}
	
	// 리뷰 삭제
	@GetMapping("/reviews/{reviewId}")
	public ResponseEntity<Void> deleteReviewById(
			@PathVariable("reviewId") Long reviewId) {
		log.info("리뷰 삭제 요청: reviewId={}", reviewId);
		reviewService.deleteReview(reviewId);
		log.info("리뷰 삭제 완료: reviewId={}", reviewId);
		return ResponseEntity.noContent().build();
	}
	
	// 본인 리뷰 목록 조회
	@GetMapping("/reviews/my")
	public ResponseEntity<List<ReviewResponse>> getMyReviews() {
		log.info("내 리뷰 목록 조회 요청");
		List<ReviewResponse> responses = reviewService.getMyReviews();
		log.info("내 리뷰 목록 조회 완료, 결과 수={}", responses.size());
		return ResponseEntity.ok(responses);
	}
	
	// 특정 유저의 리뷰 목록 조회
	@GetMapping("/users/{userId}/reviews")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<ReviewResponse>> getReviewsByUser(
			@PathVariable("userId") Long userId) {
		log.info("관리자: 사용자 리뷰 목록 조회 요청: userId={}", userId);
		List<ReviewResponse> responses = reviewService.getReviewsByUser(userId);
		log.info("관리자: 사용자 리뷰 목록 조회 완료: userId={}, 결과 수={}", userId, responses.size());
		return ResponseEntity.ok(responses);
	}
}
