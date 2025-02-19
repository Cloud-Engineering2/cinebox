package cinebox.service;

import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;

public interface MovieService {
	// create
	MovieResponse registerMovie(MovieRequest request);
}
