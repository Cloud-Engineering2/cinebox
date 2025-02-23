package cinebox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
	 
	//Optional<Seat> findBySeatNumber(String seatNumber);
    Optional<Seat> findBySeatNumberAndAuditorium_AuditoriumId(String seatNumber, Long auditoriumId);

	
}
