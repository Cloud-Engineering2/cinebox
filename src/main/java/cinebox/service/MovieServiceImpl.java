package cinebox.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cinebox.common.exception.movie.DuplicatedMovieException;
import cinebox.common.exception.movie.MovieDeleteFailedException;
import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;
import cinebox.entity.Movie;
import cinebox.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
	private final MovieRepository movieRepository;
	
	// 영화 등록(생성)
	@Override
	public MovieResponse registerMovie(MovieRequest request) {
		if (movieRepository.existsByTitleAndReleaseDate(request.title(), request.releaseDate())) {
			throw DuplicatedMovieException.EXCEPTION;
		}
		
		Movie newMovie = Movie.register(request, null);
		movieRepository.save(newMovie);
		return MovieResponse.from(newMovie);
	}

	// 영화 목록 조회 (정렬, 검색)
	@Override
	public List<MovieResponse> getAllMovies(String sortBy, String searchText) {
		List<Movie> movies;
		
		// 검색
		if (searchText != null && !searchText.isEmpty()) {
			movies = movieRepository.findByTitleContaining(searchText);
		} else {
			movies = movieRepository.findAll();
		}
		
		// 정렬
		if ("title".equals(sortBy)) {
			movies.sort(Comparator.comparing(Movie::getTitle));
		}
		return movies.stream().map(MovieResponse::from).collect(Collectors.toList());
	}

	// 특정 영화 조회
	@Override
	public MovieResponse getMovie(Long movie_id) {
		Movie movie = movieRepository.findById(movie_id)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		return MovieResponse.from(movie);
	}

	// 영화 정보 수정
	@Override
	public MovieResponse updateMovie(Long movie_id, MovieRequest request) {
		Movie movie = movieRepository.findById(movie_id)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		
		movie.updateMovie(request, null);
		Movie savedMovie = movieRepository.save(movie);
		
		return MovieResponse.from(savedMovie);
	}

	// 영화 삭제
	@Override
	public void deleteMovie(Long movie_id) {
		Movie movie = movieRepository.findById(movie_id)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		
		try {
			movieRepository.delete(movie);
		} catch (Exception e) {
			throw MovieDeleteFailedException.EXCEPTION;
		}
	}

}
