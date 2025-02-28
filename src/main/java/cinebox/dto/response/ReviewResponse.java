package cinebox.dto.response;

import java.time.LocalDateTime;

import cinebox.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponse {
	private Long reviewId;
	private Long movieId;
	private Long userId;
	private String identifier;
	private int rating;
	private String content;
	private LocalDateTime createdAt;
	
	public static ReviewResponse from(Review review) {
		return new ReviewResponse (
				review.getReviewId(),
				review.getMovie().getMovieId(),
				review.getUser().getUserId(),
				review.getUser().getIdentifier(),
				review.getRating(),
				review.getContent(),
				review.getCreatedAt()
				);
	}
}
