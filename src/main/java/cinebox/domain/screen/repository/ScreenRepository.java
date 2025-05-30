package cinebox.domain.screen.repository;

import cinebox.domain.auditorium.entity.Auditorium;
import cinebox.domain.movie.entity.Movie;
import cinebox.domain.screen.entity.Screen;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long> {

	// 상영관 내 시간 겹침 검사
	List<Screen> findByAuditoriumAndStartTimeLessThanAndEndTimeGreaterThan(Auditorium auditorium, LocalDateTime endTime, LocalDateTime startTime);

	// 현재 스크린은 제외하고, 상영관 내 시간 겹침 검사
	List<Screen> findByAuditoriumAndScreenIdNotAndStartTimeLessThanAndEndTimeGreaterThan(Auditorium auditorium, Long screenId, LocalDateTime endTime, LocalDateTime startTime);

	// 특정 날짜 하루동안의 상영 정보 조회
	List<Screen> findByMovie_MovieIdAndStartTimeBetween(Long movieId, LocalDateTime startOfDay, LocalDateTime endOfDay);

	List<Screen> findByMovie_MovieIdAndStartTimeBetweenOrderByStartTimeAsc(Long movieId, LocalDateTime startOfDay, LocalDateTime endOfDay);

	List<Screen> findByStartTimeAfter(LocalDateTime datetime);

	List<Screen> findByMovie(Movie movie);
}
