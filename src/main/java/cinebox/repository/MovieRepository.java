package cinebox.repository;

import cinebox.common.enums.MovieStatus;
import cinebox.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);

    List<Movie> findByTitleContaining(String searchText);

    List<Movie> findByStatusAndReleaseDateBefore(MovieStatus status, LocalDate releaseDate);

    boolean existsByTitleAndReleaseDateAndPosterImageUrlIsNotNull(String title, LocalDate releaseDate);
}
