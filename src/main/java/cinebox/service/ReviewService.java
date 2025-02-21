package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
		Movie movie = movieRepository.getReferenceById(reviewRequest.getMovieId());
		User user = userRepository.getReferenceById(reviewRequest.getUserId());
		Review newReview = Review.of(movie, user, reviewRequest);
		
		reviewRepository.save(newReview);
		return ReviewResponse.from(newReview);
	}

	public List<ReviewResponse> selectReviewByMovieId(Long movieId) {
		List<ReviewResponse> reviews = reviewRepository.getReviewsByMovieMovieId(movieId).stream().map(ReviewResponse::from).collect(Collectors.toList());
		return reviews;
	}
	
	public Review selectReviewByReviewId(Long reviewId) {
		return reviewRepository.getReviewsByReviewId(reviewId);
	}

	public void updateReview(ReviewRequest reviewRequest, HttpServletRequest request) {
		String token = jwtTokenProvider.getToken(request);
		Review review = selectReviewByReviewId(reviewRequest.getReviewId());
		UserResponse reviewer = userService.getUserById(review.getUser().getUserId());
		jwtTokenProvider.isUserMatchedWithToken(reviewer.getIdentifier(), token);
		
        if(reviewRepository.existsByReviewId(reviewRequest.getReviewId())) {
    		Movie movie = movieRepository.getReferenceById(reviewRequest.getMovieId());
    		User user = userRepository.getReferenceById(reviewRequest.getUserId());
    		
        	Review updatedReview = Review.of(movie, user, reviewRequest);
        	reviewRepository.save(updatedReview);
        }
	}
	
	public void deleteReview(Long reviewId, HttpServletRequest request) {
		String token = jwtTokenProvider.getToken(request);
		Review review = selectReviewByReviewId(reviewId);
		UserResponse reviewer = userService.getUserById(review.getUser().getUserId());
		jwtTokenProvider.isUserMatchedWithToken(reviewer.getIdentifier(), token);
		
        if (reviewRepository.existsByReviewId(reviewId)) {
        	reviewRepository.deleteById(reviewId);
        }
	}
}
