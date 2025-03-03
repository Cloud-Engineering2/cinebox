package cinebox.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.MovieStatus;
import cinebox.common.exception.movie.DuplicatedMovieException;
import cinebox.common.exception.movie.MovieDeleteFailedException;
import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;
import cinebox.dto.response.ScreenResponse;
import cinebox.entity.Movie;
import cinebox.entity.Screen;
import cinebox.repository.MovieRepository;
import cinebox.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
	private final MovieRepository movieRepository;
	private final ScreenRepository screenRepository;
	
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
		
		// ADMIN User가 아닌 경우 상영예정 및 상영중인 영화만 조회 가능
		if (!isAdmin()) {
	        movies = movies.stream()
	                .filter(movie -> movie.getStatus() == MovieStatus.UPCOMING || movie.getStatus() == MovieStatus.SHOWING)
	                .collect(Collectors.toList());
	    }
		
		// 정렬
		if ("title".equals(sortBy)) {
			movies.sort(Comparator.comparing(Movie::getTitle));
		}
		return movies.stream().map(MovieResponse::from).collect(Collectors.toList());
	}

	// 특정 영화 조회
	@Override
	public MovieResponse getMovie(Long movieId) {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		
		// ADMIN User가 아닌 경우, 비공개 영화 조회 불가
		if (!isAdmin() && movie.getStatus() == MovieStatus.UNRELEASED) {
			throw NotFoundMovieException.EXCEPTION;
		}
		return MovieResponse.from(movie);
	}
	
	// 특정 영화 상영 날짜 목록 조회
	@Override
	public List<LocalDate> getAvailableDatesForMovie(Long movieId) {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		
		List<Screen> screens = movie.getScreens();
		
		Set<LocalDate> dateSet = screens.stream()
				.map(screen -> screen.getStartTime().toLocalDate())
				.collect(Collectors.toSet());
		
		return dateSet.stream().sorted().collect(Collectors.toList());
	}

	// 날짜별 상영 정보 조회
	@Override
	@Transactional(readOnly = true)
	public List<ScreenResponse> getScreensByDate(Long movieId, LocalDate date) {
		movieRepository.findById(movieId).orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
		
		List<Screen> screens = screenRepository.findByMovie_MovieIdAndStartTimeBetweenOrderByStartTimeAsc(movieId, startOfDay, endOfDay);
		return screens.stream()
				.map(ScreenResponse::from)
				.collect(Collectors.toList());
	}
	
	// 영화 정보 수정
	@Override
	public MovieResponse updateMovie(Long movieId, MovieRequest request) {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		
		movie.updateMovie(request, null);
		Movie savedMovie = movieRepository.save(movie);
		
		return MovieResponse.from(savedMovie);
	}

	// 영화 삭제
	@Override
	public void deleteMovie(Long movieId) {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		
		try {
			movieRepository.delete(movie);
		} catch (Exception e) {
			throw MovieDeleteFailedException.EXCEPTION;
		}
	}

	// 사용자 권한 확인
	private boolean isAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return authentication.getAuthorities().stream()
	    		.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
	}
}
