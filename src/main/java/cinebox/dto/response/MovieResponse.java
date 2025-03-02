package cinebox.dto.response;

import java.time.LocalDate;

import cinebox.common.enums.MovieStatus;
import cinebox.entity.Movie;
import lombok.Builder;

@Builder
public record MovieResponse (
		Long movieId,
		String title,
		String plot,
		String director,
		String actors,
		String posterImageUrl,
		String genre,
		LocalDate releaseDate,
		Integer runtime,
		String ratingGrade,
		MovieStatus status,
		Integer likeCount
) {
	public static MovieResponse from(Movie movie) {
    	return new MovieResponse(
    			movie.getMovieId(),
    			movie.getTitle(),
    			movie.getPlot(),
    			movie.getDirector(),
    			movie.getActor(),
    			movie.getPosterImageUrl(),
    			movie.getGenre(),
    			movie.getReleaseDate(),
    			movie.getRunTime(),
    			movie.getRatingGrade(),
    			movie.getStatus(),
    			movie.getLikeCount());
    }
}
