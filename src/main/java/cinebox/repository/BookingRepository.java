package cinebox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Booking;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findById(Long bookingId);
}
