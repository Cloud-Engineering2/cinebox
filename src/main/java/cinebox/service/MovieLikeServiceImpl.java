package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.dto.response.MovieResponse;
import cinebox.entity.Movie;
import cinebox.entity.MovieLike;
import cinebox.entity.User;
import cinebox.repository.MovieLikeRepository;
import cinebox.repository.MovieRepository;
import cinebox.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieLikeServiceImpl implements MovieLikeService {
	
	private final MovieRepository movieRepository;
	private final MovieLikeRepository movieLikeRepository;
	
	// 영화 좋아요 등록 및 취소
	@Override
	@Transactional
	public void toggleLike(Long movieId) {
		User currentUser = SecurityUtil.getCurrentUser();
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		
		MovieLike existingLike = movieLikeRepository.findByMovieAndUser(movie, currentUser);
		if (existingLike != null) {
			movieLikeRepository.delete(existingLike);
			movie.decrementLikeCount();
			log.info("좋아요 취소: movieId={}, userId={}, likeCount={}", movieId, currentUser.getUserId(), movie.getLikeCount());
		} else {
			MovieLike like = MovieLike.createLike(movie, currentUser);
			movieLikeRepository.save(like);
			movie.incrementLikeCount();
			log.info("좋아요 등록: movieId={}, userId={}, likeCount={}", movieId, currentUser.getUserId(), movie.getLikeCount());
		}
	}

	// 본인의 좋아요 영화 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<MovieResponse> getLikedMovies() {
		User currentUser = SecurityUtil.getCurrentUser();
		List<MovieLike> likes = movieLikeRepository.findByUser(currentUser);
		
		return likes.stream()
				.map(MovieLike::getMovie)
				.distinct()
				.map(MovieResponse::from)
				.collect(Collectors.toList());
	}

}
