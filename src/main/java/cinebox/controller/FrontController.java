package cinebox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FrontController {

	// 상영시간 선택 페이지
    @GetMapping("/booking-page")
    public String showBookingPage() {
        return "booking/booking-page";  // bookingPage.html 템플릿을 반환
    }

    // 좌석 선택 페이지
    @GetMapping("/seat-selection")
    public String showSeatSelectionPage(@RequestParam("screenId") Long screenId) {
        return "booking/seat-selection";  // seatSelection.html 템플릿을 반환
    }
    
    
}
