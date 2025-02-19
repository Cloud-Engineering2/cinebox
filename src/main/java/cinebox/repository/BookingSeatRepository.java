package cinebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.BookingSeat;
import cinebox.entity.Screen;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
	 
	 // 특정 상영(screenId)의 모든 예약된 좌석 조회
    List<BookingSeat> findByScreen(Screen screen);
    
    
	 public List<BookingSeat> findByScreen_ScreenIdAndStatus(Long screenId, BookingStatus status);
	 
//	 // Screen 객체와 BookingStatus로 좌석 찾기
//	 public List<BookingSeat> findByScreenAndStatus(Screen screen, BookingStatus status);
//	
	
	 public List<BookingSeat> findByScreenAndStatus(Screen screen, BookingStatus status);

}
