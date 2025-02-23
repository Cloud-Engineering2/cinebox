package cinebox.dto.response;

import cinebox.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponse {
	private Long reviewId;
	private Long movieId;
	private Long userId;
	private int rating;
	private String content;
	
	public static ReviewResponse from(Review review) {
		return new ReviewResponse (
				review.getReviewId(),
				review.getMovie().getMovieId(),
				review.getUser().getUserId(),
				review.getRating(),
				review.getContent()
				);
	}
}
