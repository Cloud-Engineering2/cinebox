package cinebox.domain.like.entity;

import cinebox.common.entity.BaseTimeEntity;
import cinebox.domain.movie.entity.Movie;
import cinebox.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie_like", uniqueConstraints = @UniqueConstraint(columnNames = {"movie_id", "user_id"}))
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieLike extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "movie_like_id")
	private Long movieLikeId;
	
	@ManyToOne
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	public static MovieLike createLike(Movie movie, User user) {
		return MovieLike.builder()
				.movie(movie)
				.user(user)
				.build();
	}
}
