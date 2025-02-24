package cinebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{
}
