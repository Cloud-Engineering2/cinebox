package cinebox.service;

import java.util.List;

import cinebox.dto.response.MovieResponse;

public interface MovieLikeService {
	void toggleLike(Long movieId);

	List<MovieResponse> getLikedMovies();
}
