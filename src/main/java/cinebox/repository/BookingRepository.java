package cinebox.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Booking;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findById(Long bookingId);
	// 사용자 이름을 기준으로 예매 목록 조회
    List<Booking> findByUser_Identifier(String identifier);
}
