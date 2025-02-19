package cinebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Auditorium;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {

}
