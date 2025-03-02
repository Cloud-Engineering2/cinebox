package cinebox.dto.response;

import java.time.LocalDateTime;

import cinebox.entity.Review;

public record ReviewResponse(
		Long reviewId,
		Long movieId,
		String movieTitle,
		Long userId,
		String identifier,
		int rating,
		String content,
		LocalDateTime createdAt
) {
	public static ReviewResponse from(Review review) {
		return new ReviewResponse(
				review.getReviewId(),
				review.getMovie().getMovieId(),
				review.getMovie().getTitle(),
				review.getUser().getUserId(),
				review.getUser().getIdentifier(),
				review.getRating(),
				review.getContent(),
				review.getCreatedAt()
		);
	}
}