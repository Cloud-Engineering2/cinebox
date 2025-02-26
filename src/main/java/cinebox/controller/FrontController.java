package cinebox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

	
		// 예매 
		@GetMapping("/bookings/payment-test")
		public String payment() {
			return "booking/payment-test";
		}
	
}
