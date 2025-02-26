package cinebox.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cinebox.dto.response.ReviewResponse;
import cinebox.dto.response.UserResponse;
import cinebox.service.BookingService;
import cinebox.service.MovieServiceImpl;
import cinebox.service.ReviewService;
import cinebox.service.UserService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PageController {
	private final UserService userService;
	private final MovieServiceImpl movieService;
	private final BookingService bookingService;
	private final ReviewService reviewService;
	
	@GetMapping("/")
	public String login() {
		return "login";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "signup";
	}
	
	// TODO: 예매 정보 불러오기 & 좋아요 누른 영화 하는지 여쭤보기
	@GetMapping("/mypage/{userId}")
	public String detail(ModelMap map, @PathVariable("userId") Long userId) {
		
		UserResponse user = userService.getUserById(userId);
		List<ReviewResponse> reviews = reviewService.selectReviewByUserId(userId);
		
        map.addAttribute("books", null);
		map.addAttribute("user", user);
        map.addAttribute("reviews", reviews);
        
		return "mypage";
	}
	
	@GetMapping("/admin")
	public String admin(ModelMap map) {
		return "admin";
	}
}
