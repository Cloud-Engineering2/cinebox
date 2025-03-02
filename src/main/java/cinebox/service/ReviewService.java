package cinebox.service;

import cinebox.dto.request.ReviewRequest;
import cinebox.dto.response.ReviewResponse;

public interface ReviewService {

	// 리뷰 작성
	ReviewResponse createReview(Long movieId, ReviewRequest request);

}
