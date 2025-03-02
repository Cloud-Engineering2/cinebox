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

//	public ReviewResponse insertReview(ReviewRequest reviewRequest) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
//		User user = userDetails.getUser();
//		
//		Movie movie = movieRepository.findById(reviewRequest.getMovieId()).orElseThrow(()-> NotFoundMovieException.EXCEPTION);
//		Review newReview = Review.of(movie, user, reviewRequest);
//		
//		reviewRepository.save(newReview);
//		return ReviewResponse.from(newReview);
//	}
//
//	public List<ReviewResponse> selectReviewByMovieId(Long movieId) {
//		Movie movie = movieRepository.findById(movieId).orElseThrow(()-> NotFoundMovieException.EXCEPTION);
//		List<Review> reviews = movie.getReviews();
//		return reviews.stream().map(ReviewResponse::from).collect(Collectors.toList());
//	}
//	
//	public Review selectReviewByReviewId(Long reviewId) {
//		return reviewRepository.findById(reviewId).orElseThrow(()-> NotFoundReviewException.EXCEPTION);
//	}
//	
//	public List<ReviewResponse> selectReviewByUserId(Long userId) {
//		User user = User.of(userService.getUserById(userId));
//		return reviewRepository.findByUserUserId(userId).stream().map(ReviewResponse::from).collect(Collectors.toList());
//	}
//
//	public void updateReview(ReviewRequest reviewRequest) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
//		Long requestUserId = userDetails.getUser().getUserId();
//		Review review = selectReviewByReviewId(reviewRequest.getReviewId());
//		
//		if(requestUserId.equals(review.getUser().getUserId())) {
//			Movie movie = movieRepository.findById(reviewRequest.getMovieId()).orElseThrow(()-> NotFoundMovieException.EXCEPTION);
//			User user = User.of(userService.getUserById(reviewRequest.getUserId()));
//			
//	    	Review updatedReview = Review.of(movie, user, reviewRequest);
//	    	reviewRepository.save(updatedReview);	
//		} else {
//			throw NoAuthorizedUserException.EXCEPTION;
//		}
//	}
//	
//	public void deleteReview(Long reviewId) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
//		Long requestUserId = userDetails.getUser().getUserId();
//		Long reviewerId = selectReviewByReviewId(reviewId).getUser().getUserId();
//		
//        if (requestUserId.equals(reviewerId)) {
//    		reviewRepository.deleteById(reviewId);
//        } else {
//        	throw NoAuthorizedUserException.EXCEPTION;
//        }
//	}
}
