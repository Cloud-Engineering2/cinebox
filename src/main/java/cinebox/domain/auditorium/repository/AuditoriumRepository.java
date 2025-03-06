package cinebox.domain.auditorium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cinebox.domain.auditorium.entity.Auditorium;

import java.util.Optional;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Long> {

    @Query("SELECT a FROM Auditorium a LEFT JOIN FETCH a.screens WHERE a.auditoriumId = :auditoriumId")
    Optional<Auditorium> findByIdWithScreens(@Param("auditoriumId") Long auditoriumId);
}
