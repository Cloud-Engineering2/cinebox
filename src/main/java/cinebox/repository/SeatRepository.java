package cinebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

}
