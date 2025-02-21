package cinebox.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cinebox.entity.Screen;

public interface ScreenRepositoryTest extends JpaRepository<Screen, Long> {

	    // 특정 영화 ID에 해당하는 상영 정보를 조회하는 메서드
	    List<Screen> findByMovie_MovieId(Long movieId);
}
