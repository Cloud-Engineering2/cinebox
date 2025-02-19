package cinebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.entity.Seat;

public interface SeatRepository  extends JpaRepository<Seat, Long> { 

	// 특정 상영에 속한 모든 좌석 조회
    List<Seat> findByScreen_ScreenId(Long screenId);
    
    List<Seat> findByScreen_ScreenIdAndBookingSeatsIsNull(Long screenId); // 예약되지 않은 좌석 찾기 
}
