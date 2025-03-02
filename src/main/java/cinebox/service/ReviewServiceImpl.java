package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.exception.review.NotFoundReviewException;
import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.dto.request.ReviewRequest;
import cinebox.dto.response.ReviewResponse;
import cinebox.entity.Movie;
import cinebox.entity.Review;
import cinebox.entity.User;
import cinebox.repository.MovieRepository;
import cinebox.repository.ReviewRepository;
import cinebox.repository.UserRepository;
import cinebox.security.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final UserRepository userRepository;
	private final MovieRepository movieRepository;
	private final ReviewRepository reviewRepository;

	@Override
	@Transactional
	public ReviewResponse createReview(Long movieId, ReviewRequest request) {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		User currentUser = SecurityUtil.getCurrentUser();
		
		Review review = Review.createReview(movie, currentUser, request);
		reviewRepository.save(review);
		
		return ReviewResponse.from(review);
	}

	@Override
	@Transactional
	public ReviewResponse updateReview(Long reviewId, ReviewRequest request) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> NotFoundReviewException.EXCEPTION);
		User currentUser = SecurityUtil.getCurrentUser();
		User reviewUser = review.getUser();
		
		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(reviewUser.getUserId())) {
			throw NoAuthorizedUserException.EXCEPTION;
		}
		
		review.updateReview(request);
		Review savedReview = reviewRepository.save(review);
		
		return ReviewResponse.from(savedReview);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getReviewsByMovieId(Long movieId) {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		List<Review> reviews = movie.getReviews();
		
		return reviews.stream()
				.map(ReviewResponse::from)
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
				.orElseThrow(() -> NotFoundReviewException.EXCEPTION);
		
		User currentUser = SecurityUtil.getCurrentUser();
		User reviewUser = review.getUser();
		
		if (!SecurityUtil.isAdmin() && !currentUser.getUserId().equals(reviewUser.getUserId())) {
			throw NoAuthorizedUserException.EXCEPTION;
		}
		reviewRepository.delete(review);
	}

	// 본인 리뷰 조회
	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getMyReviews() {
		User currentUser = SecurityUtil.getCurrentUser();
		
		return getReviewsByUserId(currentUser.getUserId());
	}

	// 특정 유저의 리뷰 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<ReviewResponse> getReviewsByUser(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> NotFoundUserException.EXCEPTION);
		return getReviewsByUserId(user.getUserId());
	}

	private List<ReviewResponse> getReviewsByUserId(Long userId) {
		List<Review> reviews = reviewRepository.findByUserUserId(userId);
		return reviews.stream()
				.map(ReviewResponse::from)
				.collect(Collectors.toList());
	}
}
