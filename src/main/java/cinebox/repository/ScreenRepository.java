package cinebox.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Screen;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

}
