package cinebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Auditorium;

import cinebox.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
	
	List<Seat> findByAuditorium(Auditorium auditorium);
	
	List<Seat> findByAuditorium_AuditoriumId(Long auditoriumId);
}
