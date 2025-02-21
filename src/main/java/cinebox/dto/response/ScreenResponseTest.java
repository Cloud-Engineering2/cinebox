package cinebox.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cinebox.entity.Auditorium;
import cinebox.entity.Movie;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreenResponseTest {
	
		private Long screenId;
	    private Movie movie;               // 영화 객체
	    private Auditorium auditorium;      // 상영관 객체
	    private LocalDateTime startTime;
	    private LocalDateTime endTime;
	    private BigDecimal price;
	    private List<SeatResponse> availableSeats; // 예매 가능한 좌석
	  
	    // 생성자 추가
	    public ScreenResponseTest(Long screenId, LocalDateTime startTime) {
	        this.screenId = screenId;
	        this.startTime = startTime;
	    }
	    
	    
	    public ScreenResponseTest(Screen screen) {
	        this.screenId = screen.getScreenId();
	        this.movie = screen.getMovie();  // Movie 객체 그대로 전달
	        this.auditorium = screen.getAuditorium();  // Auditorium 객체 그대로 전달
	        this.startTime = screen.getStartTime();
	        this.endTime = screen.getEndTime();
	        this.price = screen.getPrice();
	        // Auditorium을 통해 Seat 정보 가져오기
	        this.availableSeats = getAvailableSeatsFromAuditorium(screen.getAuditorium());
	      
	    }
	    
	    
	    // Auditorium을 통해 Seat 정보를 조회하는 메소드
	    private List<SeatResponse> getAvailableSeatsFromAuditorium(Auditorium auditorium) {
	        // Auditorium 객체에서 좌석을 가져오는 로직을 구현
	        List<Seat> seats = auditorium.getSeats(); // Auditorium에 연결된 좌석들을 가져옵니다.
	        List<SeatResponse> seatResponses = new ArrayList<>();
	        for (Seat seat : seats) {
	            seatResponses.add(new SeatResponse(seat)); // 각 Seat 객체를 SeatResponse로 변환
	        }
	        return seatResponses;
	    }
	    
}
