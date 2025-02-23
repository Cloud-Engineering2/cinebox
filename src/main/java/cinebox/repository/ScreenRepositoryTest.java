package cinebox.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Screen;

@Repository
public interface ScreenRepositoryTest extends JpaRepository<Screen, Long>{
	Optional<Screen> findById(Long id);
}
