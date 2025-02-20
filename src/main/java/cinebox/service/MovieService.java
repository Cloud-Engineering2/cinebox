package cinebox.service;

import java.util.List;

import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;

public interface MovieService {
	// create
	MovieResponse registerMovie(MovieRequest request);
	
	// read
	List<MovieResponse> getAllMovies(String sortBy, String searchText);
	
	MovieResponse getMovie(Long movie_id);
	
	// update
	MovieResponse updateMovie(Long movie_id, MovieRequest request);

	// delete
	void deleteMovie(Long movie_id);
}
