package cinebox.domain.auditorium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.domain.auditorium.entity.Auditorium;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {

	boolean existsByName(String name);
}
