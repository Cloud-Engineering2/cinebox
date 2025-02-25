package cinebox.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

	private Long bookingId;
	private LocalDateTime bookingDate;
	private Long screenId;
	private List<String> seatNumbers;
	private String status;
	private BigDecimal totalPrice; // 여기서 totalPrice를 추가
	private String message;
	private String screenName; // 상영관 이름 추가

	public BookingResponse(Long bookingId, LocalDateTime bookingDate, Long screenId, List<String> seatNumbers,
			String status, BigDecimal totalPrice, String screenName) {
		this.bookingId = bookingId;
		this.bookingDate = bookingDate;
		this.screenId = screenId;
		this.seatNumbers = seatNumbers;
		this.status = status;
		this.totalPrice = totalPrice;
		this.screenName = screenName;
	}

}