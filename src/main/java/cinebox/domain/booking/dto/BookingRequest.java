package cinebox.domain.booking.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {

	private Long screenId;
	private List<String> seatNumbers; // 좌석번호 (예: ["A1", "A2", "B1"]

}