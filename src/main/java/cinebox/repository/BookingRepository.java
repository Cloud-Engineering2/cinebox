package cinebox.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByUser_UserIdAndStatusIn(Long userId, Collection<BookingStatus> statuses);
}
