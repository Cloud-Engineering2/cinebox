package cinebox.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingSeatResponse {

	 	private Long seatId;
	    private Long bookingId;
	    private Long screenId;
	    private String seatNumber;

	    
}
