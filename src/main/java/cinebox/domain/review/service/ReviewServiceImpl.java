package cinebox.domain.review.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.exception.review.NotFoundReviewException;
import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.common.utils.SecurityUtil;
import cinebox.domain.movie.entity.Movie;
import cinebox.domain.movie.repository.MovieRepository;
import cinebox.domain.review.dto.ReviewRequest;
import cinebox.domain.review.dto.ReviewResponse;
import cinebox.domain.review.entity.Review;
import cinebox.domain.review.repository.ReviewRepository;
import cinebox.domain.user.entity.User;
import cinebox.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final UserRepository userRepository;
	private final MovieRepository movieRepository;
	private final ReviewRepository reviewRepository;

	@Override
	@Transactional
	public ReviewResponse createReview(Long movieId, ReviewRequest request) {
		log.info("리뷰 생성 서비스 시작: movieId={}", movieId);
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		User currentUser = SecurityUtil.getCurrentUser();

		Review review = Review.createReview(movie, currentUser, request);
		reviewRepository.save(review);

		log.info("리뷰 생성 완료: reviewId={}", review.getReviewId());
		return ReviewResponse.from(review);
	}

	@Override
	@Transactional
	public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {
		log.info("리뷰 수정 서비스 시작: reviewId={}", reviewId);
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> NotFoundReviewException.EXCEPTION);
		User currentUser = SecurityUtil.getCurrentUser();
		User reviewUser = review.getUser();

		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(reviewUser.getUserId())) {
			log.error("리뷰 수정 권한 없음: currentUserId={}, reviewUserId={}", currentUser.getUserId(), reviewUser.getUserId());
			throw NoAuthorizedUserException.EXCEPTION;
		}

		review.updateReview(request);
		Review savedReview = reviewRepository.save(review);

		log.info("리뷰 수정 완료: reviewId={}", savedReview.getReviewId());
		return ReviewResponse.from(savedReview);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getReviewsByMovieId(Long movieId) {
		log.info("영화 리뷰 목록 조회 서비스 시작: movieId={}", movieId);
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		List<Review> reviews = movie.getReviews();
		
		List<ReviewResponse> responses = reviews.stream()
				.map(ReviewResponse::from)
				.collect(Collectors.toList());
		log.info("영화 리뷰 목록 조회 서비스 완료: movieId={}, 결과 수={}", movieId, responses.size());
		return responses;
	}

	@Override
	@Transactional
	public void deleteReview(Long reviewId) {
		log.info("리뷰 삭제 서비스 시작: reviewId={}", reviewId);
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> NotFoundReviewException.EXCEPTION);
		
		User currentUser = SecurityUtil.getCurrentUser();
		User reviewUser = review.getUser();
		
		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(reviewUser.getUserId())) {
			log.error("리뷰 삭제 권한 없음: currentUserId={}, reviewUserId={}", currentUser.getUserId(), reviewUser.getUserId());
			throw NoAuthorizedUserException.EXCEPTION;
		}
		reviewRepository.delete(review);
		log.info("리뷰 삭제 완료: reviewId={}", reviewId);
	}

	// 본인 리뷰 조회
	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getMyReviews() {
		log.info("내 리뷰 목록 조회 서비스 시작");
		User currentUser = SecurityUtil.getCurrentUser();
		return getReviewsByUserId(currentUser.getUserId());
	}

	// 특정 유저의 리뷰 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getReviewsByUser(Long userId) {
		log.info("사용자 리뷰 목록 조회 서비스 시작: userId={}", userId);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> NotFoundUserException.EXCEPTION);
		return getReviewsByUserId(user.getUserId());
	}

	private List<ReviewResponse> getReviewsByUserId(Long userId) {
		List<Review> reviews = reviewRepository.findByUserUserId(userId);
		List<ReviewResponse> responses = reviews.stream()
				.map(ReviewResponse::from)
				.collect(Collectors.toList());
		log.info("사용자 리뷰 목록 조회 완료: userId={}, 결과 수={}", userId, responses.size());
		return responses;
	}
}
