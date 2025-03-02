package cinebox.service;

import java.util.List;

import cinebox.dto.request.ReviewRequest;
import cinebox.dto.response.ReviewResponse;

public interface ReviewService {

	// 리뷰 작성
	ReviewResponse createReview(Long movieId, ReviewRequest request);

	// 리뷰 수정
	ReviewResponse updateReview(Long reviewId, ReviewRequest request);

	// 특정 영화 리뷰 목록 조회
	List<ReviewResponse> getReviewsByMovieId(Long movieId);

	// 영화 삭제
	void deleteReview(Long reviewId);

	// 본인 리뷰 조회
	List<ReviewResponse> getMyReviews();

	// 특정 유저의 리뷰 목록 조회
	List<ReviewResponse> getReviewsByUser(Long userId);

}
