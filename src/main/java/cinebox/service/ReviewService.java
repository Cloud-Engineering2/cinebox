package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.exception.review.NotFoundReviewException;
import cinebox.common.exception.user.NoAuthorizedUserException;
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
import cinebox.security.PrincipalDetails;
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

	public void updateReview(ReviewRequest reviewRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
		Long requestUserId = userDetails.getUser().getUserId();
		Review review = selectReviewByReviewId(reviewRequest.getReviewId());
		
		if(requestUserId.equals(review.getUser().getUserId())) {
			Movie movie = movieRepository.findById(reviewRequest.getMovieId()).orElseThrow(()-> NotFoundMovieException.EXCEPTION);
			User user = userRepository.findById(reviewRequest.getUserId()).orElseThrow(()-> NotFoundUserException.EXCEPTION);
			
	    	Review updatedReview = Review.of(movie, user, reviewRequest);
	    	reviewRepository.save(updatedReview);	
		} else {
			throw NoAuthorizedUserException.EXCEPTION;
		}
	}
	
	public void deleteReview(Long reviewId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
		Long requestUserId = userDetails.getUser().getUserId();
		Long reviewerId = selectReviewByReviewId(reviewId).getUser().getUserId();
		
        if (requestUserId.equals(reviewerId)) {
    		reviewRepository.deleteById(reviewId);
        } else {
        	throw NoAuthorizedUserException.EXCEPTION;
        }
	}
}
