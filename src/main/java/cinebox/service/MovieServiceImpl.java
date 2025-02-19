package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cinebox.common.exception.movie.DuplicatedMovieException;
import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;
import cinebox.entity.Movie;
import cinebox.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
	private final MovieRepository movieRepository;
	
	@Override
	public MovieResponse registerMovie(MovieRequest request) {
		if (movieRepository.existsByTitleAndReleaseDate(request.title(), request.releaseDate())) {
			throw DuplicatedMovieException.EXCEPTION;
		}
		
		Movie newMovie = Movie.register(request, null);
		movieRepository.save(newMovie);
		return MovieResponse.from(newMovie);
	}

	@Override
	public List<MovieResponse> getAllMovies() {
		List<Movie> movies = movieRepository.findAll();
		return movies.stream().map(MovieResponse::from).collect(Collectors.toList());
	}

}
