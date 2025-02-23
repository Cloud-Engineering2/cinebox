package cinebox.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.common.enums.MovieStatus;
import cinebox.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
	boolean existsByTitleAndReleaseDate(String title, LocalDate relaseDate);
	
	List<Movie> findByTitleContaining(String searchText);
	
	List<Movie> findByStatusAndReleaseDate(MovieStatus status, LocalDate releaseDate);
}
