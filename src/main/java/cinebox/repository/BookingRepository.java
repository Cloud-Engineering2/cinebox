package cinebox.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByStatusAndBookingDateBefore(BookingStatus status, LocalDateTime cutoff);

	List<Booking> findByUser_UserIdAndStatusIn(Long userId, Collection<BookingStatus> statuses);

	// 사용자 이름을 기준으로 예매 목록 조회
	List<Booking> findByUser_Identifier(String identifier);
}
