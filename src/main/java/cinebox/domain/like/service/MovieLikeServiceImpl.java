package cinebox.domain.like.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.utils.SecurityUtil;
import cinebox.domain.like.entity.MovieLike;
import cinebox.domain.like.repository.MovieLikeRepository;
import cinebox.domain.movie.dto.MovieResponse;
import cinebox.domain.movie.entity.Movie;
import cinebox.domain.movie.repository.MovieRepository;
import cinebox.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieLikeServiceImpl implements MovieLikeService {
	
	private final MovieRepository movieRepository;
	private final MovieLikeRepository movieLikeRepository;
	
	// 영화 좋아요 등록 및 취소
	@Override
	@Transactional
	public void toggleLike(Long movieId) {
		User currentUser = SecurityUtil.getCurrentUser();
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> {
					log.error("서비스: 영화를 찾을 수 없음: movieId={}", movieId);
					return NotFoundMovieException.EXCEPTION;
				});
		
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
		log.info("서비스: 좋아요 영화 목록 조회 시작");
		User currentUser = SecurityUtil.getCurrentUser();
		List<MovieLike> likes = movieLikeRepository.findByUser(currentUser);
		
		List<MovieResponse> responses = likes.stream()
				.map(MovieLike::getMovie)
				.distinct()
				.map(MovieResponse::from)
				.collect(Collectors.toList());
		log.info("서비스: 좋아요 영화 목록 조회 완료, 결과 수: {}", responses.size());
		return responses;
	}

}
