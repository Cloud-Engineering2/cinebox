package cinebox.domain.seat.repository;


import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.domain.auditorium.entity.Auditorium;
import cinebox.domain.seat.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

  Optional<Seat> findBySeatNumberAndAuditorium_AuditoriumId(String seatNumber, Long auditoriumId);
	
	List<Seat> findByAuditorium(Auditorium auditorium);
	
	List<Seat> findByAuditorium_AuditoriumId(Long auditoriumId);

}
