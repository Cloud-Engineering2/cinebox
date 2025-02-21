package cinebox.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class AuditoriumResponse {

    private Long auditoriumId;
    private String name;
    private List<SeatResponse> availableSeats;
    private ScreenResponseTest screenResponse;

    // 생성자 추가
    public AuditoriumResponse(Long auditoriumId, String name, List<SeatResponse> availableSeats, ScreenResponseTest screenResponse) {
        this.auditoriumId = auditoriumId;
        this.name = name;
        this.availableSeats = availableSeats;
        this.screenResponse = screenResponse;
    }

  
    
}
