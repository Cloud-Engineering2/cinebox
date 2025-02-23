package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.exception.review.NotFoundReviewException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.dto.request.ReviewRequest;
import cinebox.dto.response.ReviewResponse;
import cinebox.dto.response.UserResponse;
import cinebox.entity.Movie;
import cinebox.entity.Review;
import cinebox.entity.User;
import cinebox.repository.MovieRepository;
import cinebox.repository.ReviewRepository;
import cinebox.repository.UserRepository;
import cinebox.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	
	private final UserRepository userRepository;
	private final MovieRepository movieRepository;
	private final ReviewRepository reviewRepository;

	public ReviewResponse insertReview(ReviewRequest reviewRequest) {
		Movie movie = movieRepository.findById(reviewRequest.getMovieId()).orElseThrow(()-> NotFoundMovieException.EXCEPTION);
		User user = userRepository.findById(reviewRequest.getUserId()).orElseThrow(()-> NotFoundUserException.EXCEPTION);
		Review newReview = Review.of(movie, user, reviewRequest);
		
		reviewRepository.save(newReview);
		return ReviewResponse.from(newReview);
	}

	public List<ReviewResponse> selectReviewByMovieId(Long movieId) {
		Movie movie = movieRepository.findById(movieId).orElseThrow(()-> NotFoundMovieException.EXCEPTION);
		List<Review> reviews = movie.getReviews();
		return reviews.stream().map(ReviewResponse::from).collect(Collectors.toList());
	}
	
	public Review selectReviewByReviewId(Long reviewId) {
		return reviewRepository.findById(reviewId).orElseThrow(()-> NotFoundReviewException.EXCEPTION);
	}

	public void updateReview(ReviewRequest reviewRequest, HttpServletRequest request) {
		String token = jwtTokenProvider.getToken(request);
		Review review = selectReviewByReviewId(reviewRequest.getReviewId());
		UserResponse reviewer = userService.getUserById(review.getUser().getUserId());
		jwtTokenProvider.isUserMatchedWithToken(reviewer.getIdentifier(), token);
		
		Movie movie = movieRepository.findById(reviewRequest.getMovieId()).orElseThrow(()-> NotFoundMovieException.EXCEPTION);
		User user = userRepository.findById(reviewRequest.getUserId()).orElseThrow(()-> NotFoundUserException.EXCEPTION);
		
    	Review updatedReview = Review.of(movie, user, reviewRequest);
    	reviewRepository.save(updatedReview);
	}
	
	public void deleteReview(Long reviewId, HttpServletRequest request) {
		String token = jwtTokenProvider.getToken(request);
		Review review = selectReviewByReviewId(reviewId);
		UserResponse reviewer = userService.getUserById(review.getUser().getUserId());
		jwtTokenProvider.isUserMatchedWithToken(reviewer.getIdentifier(), token);
		
		reviewRepository.deleteById(reviewId);
	}
}
