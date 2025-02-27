package cinebox.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUser_UserIdAndStatusIn(Long userId, Collection<BookingStatus> statuses);

	Optional<Booking> findById(Long bookingId);
	// 사용자 이름을 기준으로 예매 목록 조회
    List<Booking> findByUser_Identifier(String identifier);
}
