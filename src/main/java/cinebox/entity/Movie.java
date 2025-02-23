package cinebox.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cinebox.common.enums.MovieStatus;
import cinebox.dto.request.MovieRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "movie_id")
	private Long movieId;

	@Column(nullable = false)
	private String title;

	// 줄거리 (description)
	private String plot;

	private String director;
	private String actor;
	private String genre;

	@Column(name = "poster_image_url")
	private String posterImageUrl;

	@Column(name = "release_date")
	private LocalDate releaseDate;

	@Column(name = "run_time")
	private Integer runTime;

	@Column(name = "rating_grade")
	private String ratingGrade;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MovieStatus status;

	@OneToMany(mappedBy = "movie")
	private List<Screen> screens = new ArrayList<>();

	@OneToMany(mappedBy = "movie")
	private List<Review> reviews = new ArrayList<>();
	
	public static Movie register(MovieRequest request, String posterImageUrl) {
		return Movie.builder()
				.title(request.title())
				.plot(request.plot())
				.director(request.director())
				.actor(request.actors())
				.genre(request.genre())
				.posterImageUrl(posterImageUrl)
				.releaseDate(request.releaseDate())
				.runTime(request.runtime())
				.ratingGrade(request.ratingGrade())
				.status(request.status() != null ? request.status() : MovieStatus.UNRELEASED)
				.build();
	}
	
	public void updateMovie(MovieRequest request, String newPosterImageUrl) {
		this.title = request.title() != null ? request.title() : this.title;
		this.plot = request.plot() != null ? request.plot() : this.plot;
		this.director = request.director() != null ? request.director() : this.director;
		this.actor = request.actors() != null ? request.actors() : this.actor;
		this.genre = request.genre() != null ? request.genre() : this.genre;
		this.posterImageUrl = newPosterImageUrl != null ? newPosterImageUrl : this.posterImageUrl;
		this.releaseDate = request.releaseDate() != null ? request.releaseDate() : this.releaseDate;
		this.runTime = request.runtime() != null ? request.runtime() : this.runTime;
		this.ratingGrade = request.ratingGrade() != null ? request.ratingGrade() : this.ratingGrade;
		this.status = request.status() != null ? request.status() : this.status;
	}
	
	public void updateMovieStatus(MovieStatus status) {
		this.status = status;
	}
}
