package cinebox.domain.like.service;

import java.util.List;

import cinebox.domain.movie.dto.MovieResponse;

public interface MovieLikeService {
	void toggleLike(Long movieId);

	List<MovieResponse> getLikedMovies();
}
