package cinebox.domain.like.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cinebox.domain.like.entity.MovieLike;
import cinebox.domain.movie.entity.Movie;
import cinebox.domain.user.entity.User;

public interface MovieLikeRepository extends JpaRepository<MovieLike, Long>{

	MovieLike findByMovieAndUser(Movie movie, User user);

	List<MovieLike> findByUser(User user);

}
