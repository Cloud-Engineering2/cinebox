package cinebox.domain.movie.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.MovieStatus;
import cinebox.common.exception.movie.DuplicatedMovieException;
import cinebox.common.exception.movie.MovieDeleteFailedException;
import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.utils.SecurityUtil;
import cinebox.domain.movie.dto.MovieRequest;
import cinebox.domain.movie.dto.MovieResponse;
import cinebox.domain.movie.entity.Movie;
import cinebox.domain.movie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
	private final MovieRepository movieRepository;
	
	// 영화 등록(생성)
	@Override
	@Transactional
	public MovieResponse registerMovie(MovieRequest request, String posterImageUrl) {
		log.info("영화 등록 요청: title={}, releaseDate={}", request.title(), request.releaseDate());
		if (movieRepository.existsByTitleAndReleaseDate(request.title(), request.releaseDate())) {
			log.error("영화 등록 실패: 이미 존재하는 영화 - title={}, releaseDate={}", request.title(), request.releaseDate());
			throw DuplicatedMovieException.EXCEPTION;
		}
		
		Movie newMovie = Movie.register(request, posterImageUrl);
		movieRepository.save(newMovie);
		
		log.info("영화 등록 성공: movieId={}", newMovie.getMovieId());
		return MovieResponse.from(newMovie);
	}

	// 영화 목록 조회 (정렬, 검색)
	@Override
	@Transactional(readOnly = true)
	public List<MovieResponse> getAllMovies(String sortBy, String searchText) {
		log.info("영화 목록 조회 요청: sortBy={}, searchText={}", sortBy, searchText);
		List<Movie> movies;
		
		// 검색
		if (searchText != null && !searchText.isEmpty()) {
			movies = movieRepository.findByTitleContaining(searchText);
		} else {
			movies = movieRepository.findAll();
		}
		
		// ADMIN User가 아닌 경우 상영예정 및 상영중인 영화만 조회 가능
		if (!SecurityUtil.isAdmin()) {
	        movies = movies.stream()
	                .filter(movie -> movie.getStatus() == MovieStatus.UPCOMING || movie.getStatus() == MovieStatus.SHOWING)
	                .collect(Collectors.toList());
	    }
		
		// 정렬
		if ("title".equals(sortBy)) {
			movies.sort(Comparator.comparing(Movie::getTitle));
		}
		
		List<MovieResponse> responses = movies.stream().map(MovieResponse::from).collect(Collectors.toList());
		log.info("영화 목록 조회 완료, 결과 수: {}", responses.size());
		return responses;
	}

	// 특정 영화 조회
	@Override
	@Transactional(readOnly = true)
	public MovieResponse getMovie(Long movieId) {
		log.info("특정 영화 조회 요청: movieId={}", movieId);
		Movie movie = getMovieById(movieId);
		
		// ADMIN User가 아닌 경우, 비공개 영화 조회 불가
		if (!SecurityUtil.isAdmin() && movie.getStatus() == MovieStatus.UNRELEASED) {
			log.error("영화 조회 실패: 비공개 영화 - movieId={}", movieId);
			throw NotFoundMovieException.EXCEPTION;
		}
		
		log.info("특정 영화 조회 성공: movieId={}", movieId);
		return MovieResponse.from(movie);
	}
	
	// 영화 정보 수정
	@Override
	@Transactional
	public MovieResponse updateMovie(Long movieId, MovieRequest request, String posterImageUrl) {
		log.info("영화 수정 요청: movieId={}, title={}", movieId, request.title());
		Movie movie = getMovieById(movieId);
		
		movie.updateMovie(request, posterImageUrl);
		Movie savedMovie = movieRepository.save(movie);
		
		log.info("영화 수정 성공: movieId={}", savedMovie.getMovieId());
		return MovieResponse.from(savedMovie);
	}

	// 영화 삭제
	@Override
	@Transactional
	public void deleteMovie(Long movieId) {
		log.info("영화 삭제 요청: movieId={}", movieId);
		Movie movie = getMovieById(movieId);
		
		try {
			movieRepository.delete(movie);
			log.info("영화 삭제 성공: movieId={}", movieId);
		} catch (Exception e) {
			log.error("영화 삭제 실패: movieId={}, 에러={}", movieId, e.getMessage());
			throw MovieDeleteFailedException.EXCEPTION;
		}
	}
	
	private Movie getMovieById(Long movieId) {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> {
					log.error("영화 수정 실패: movieId={} 조회 결과 없음", movieId);
					return NotFoundMovieException.EXCEPTION;
				});
		return movie;
	}
}
