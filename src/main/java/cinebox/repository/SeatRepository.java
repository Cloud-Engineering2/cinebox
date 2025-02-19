package cinebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.entity.Seat;

public interface SeatRepository  extends JpaRepository<Seat, Long> { 

	List<Seat> findByAuditorium_AuditoriumId(Long auditoriumId);
}
