package cinebox.entity;

import cinebox.dto.request.ReviewRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId;

	@ManyToOne
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private Integer rating;

	private String content;
	
	public static Review createReview(Movie movie, User user, ReviewRequest request ) {
		return Review.builder()
				.movie(movie)
				.user(user)
				.rating(request.rating())
				.content(request.content())
				.build();
	}
	
	public void updateReview(ReviewRequest request) {
		this.rating = request.rating() != null ? request.rating() : this.rating;
		this.content = request.content() != null ? request.content() : this.content;
	}
}
