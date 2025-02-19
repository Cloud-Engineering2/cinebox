package cinebox.service;

import java.util.List;

import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;

public interface MovieService {
	// create
	MovieResponse registerMovie(MovieRequest request);
	
	// read
	List<MovieResponse> getAllMovies();
}
