package cinebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking,Long>{

}
