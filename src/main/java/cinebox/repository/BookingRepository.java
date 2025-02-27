package cinebox.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.Booking;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	List<Booking> findByStatusAndBookingDateBefore(BookingStatus status, LocalDateTime cutoff);
}
