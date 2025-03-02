package cinebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.entity.Movie;
import cinebox.entity.MovieLike;
import cinebox.entity.User;

public interface MovieLikeRepository extends JpaRepository<MovieLike, Long>{

	MovieLike findByMovieAndUser(Movie movie, User user);

	List<MovieLike> findByUser(User user);

}
