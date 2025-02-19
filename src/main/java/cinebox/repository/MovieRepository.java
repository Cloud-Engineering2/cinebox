package cinebox.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	boolean existsByTitleAndReleaseDate(String title, LocalDate relaseDate);
}
