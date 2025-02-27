package cinebox.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cinebox.entity.Booking;
import cinebox.entity.BookingSeat;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {


	List<BookingSeat> findByScreen_ScreenId(Long screenId);

	// BookingSeatRepository에 추가할 메서드 예시
	List<BookingSeat> findByScreen_ScreenIdAndScreen_StartTime(Long screenId, LocalDateTime startTime);

	// 좌석 번호 리스트를 받아서 해당 좌석들이 예약된 경우를 조회하는 메서드
	List<BookingSeat> findBySeat_SeatNumberIn(List<String> seatNumbers);

	// 상영회차 ID와 좌석 번호 리스트를 기준으로 예약된 좌석을 조회
	List<BookingSeat> findByScreen_ScreenIdAndSeat_SeatNumberIn(Long screenId, List<String> seatNumbers);

	// screenId와 startTime에 맞는 좌석 번호를 찾아서 예약된 좌석 리스트 반환
  List<BookingSeat> findByScreen_ScreenIdAndScreen_StartTimeAndSeat_SeatNumberIn(Long screenId, LocalDateTime startTime, List<String> seatNumbers);

  @Query("SELECT bs.seat.seatId FROM BookingSeat bs WHERE bs.screen.screenId = :screenId")
  List<Long> findBookedSeatIdsByScreenId(@Param("screenId") Long screenId);

  void deleteByBooking(Booking booking);

}
