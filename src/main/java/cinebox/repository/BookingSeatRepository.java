package cinebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cinebox.entity.BookingSeat;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    @Query("SELECT bs.seat.seatId FROM BookingSeat bs WHERE bs.screen.screenId = :screenId")
    List<Long> findBookedSeatIdsByScreenId(@Param("screenId") Long screenId);

}
