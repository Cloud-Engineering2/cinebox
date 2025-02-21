
package cinebox.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@Builder
public class ShowtimeWithSeatsResponse {
	
	private Long screenId;
    private LocalDateTime startTime;
    private List<SeatResponse> availableSeats;
    

    public ShowtimeWithSeatsResponse(Long screenId, LocalDateTime startTime, List<SeatResponse> availableSeats) {
        this.screenId = screenId;
        this.startTime = startTime;
        this.availableSeats = availableSeats;
    }
}
