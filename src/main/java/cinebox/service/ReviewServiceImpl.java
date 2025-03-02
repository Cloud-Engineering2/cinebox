package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.exception.review.NotFoundReviewException;
import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.dto.request.ReviewRequest;
import cinebox.dto.response.ReviewResponse;
import cinebox.entity.Movie;
import cinebox.entity.Review;
import cinebox.entity.User;
import cinebox.repository.MovieRepository;
import cinebox.repository.ReviewRepository;
import cinebox.security.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final UserService userService;
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
	@Transactional
	public List<ReviewResponse> getReviewsByMovieId(Long movieId) {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);
		List<Review> reviews = movie.getReviews();
		
		return reviews.stream()
				.map(ReviewResponse::from)
				.collect(Collectors.toList());
	}

	@Override
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
}
