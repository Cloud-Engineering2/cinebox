package cinebox.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.auditorium.NotFoundAuditoriumException;
import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.exception.screen.NotFoundScreenException;
import cinebox.common.exception.screen.ScreenTimeConflictException;
import cinebox.dto.request.ScreenRequest;
import cinebox.dto.response.ScreenResponse;
import cinebox.entity.Auditorium;
import cinebox.entity.Movie;
import cinebox.entity.Screen;
import cinebox.repository.AuditoriumRepository;
import cinebox.repository.MovieRepository;
import cinebox.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {
	private final ScreenRepository screenRepository;
	private final MovieRepository movieRepository;
	private final AuditoriumRepository auditoriumRepository;

	// 상영 정보 추가
	@Override
	@Transactional
	public ScreenResponse createScreen(ScreenRequest request) {
		Movie movie = movieRepository.findById(request.movieId())
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);

		Auditorium auditorium = auditoriumRepository.findById(request.auditoriumId())
				.orElseThrow(() -> NotFoundAuditoriumException.EXCEPTION);

		LocalDateTime endTime = request.startTime().plusMinutes(movie.getRunTime() + 10); // 🎯 endTime 자동 계산
		
		List<Screen> overlappingScreens = screenRepository
				.findByAuditoriumAndStartTimeLessThanAndEndTimeGreaterThan(auditorium, endTime, request.startTime());
		if(!overlappingScreens.isEmpty()) {
			throw ScreenTimeConflictException.EXCEPTION;
		}

		Screen screen = Screen.builder()
				.movie(movie)
				.auditorium(auditorium)
				.startTime(request.startTime())
				.endTime(endTime)
				.price(request.price())
				.build();

		screenRepository.save(screen);
		return ScreenResponse.from(screen);
	}

	// 상영 정보 수정
	@Override
	@Transactional
	public ScreenResponse updateScreen(Long screenId, ScreenRequest request) {
		Screen screen = screenRepository.findById(screenId).orElseThrow(() -> NotFoundScreenException.EXCEPTION);

		Movie movie = request.movieId() != null
				? movieRepository.findById(request.movieId())
						.orElseThrow(() -> NotFoundMovieException.EXCEPTION)
				: screen.getMovie();
		
		Auditorium auditorium = request.auditoriumId() != null 
	            ? auditoriumRepository.findById(request.auditoriumId())
						.orElseThrow(() -> NotFoundAuditoriumException.EXCEPTION)
				: screen.getAuditorium();
		
		LocalDateTime startTime = request.startTime() != null ? request.startTime() : screen.getStartTime();
		LocalDateTime endTime = startTime.plusMinutes(movie.getRunTime() + 10);
		
		List<Screen> overlappingScreens = screenRepository
				.findByAuditoriumAndScreenIdNotAndStartTimeLessThanAndEndTimeGreaterThan(auditorium, screenId, endTime, startTime);
        if (!overlappingScreens.isEmpty()) {
            throw ScreenTimeConflictException.EXCEPTION;
        }

		screen.updateScreen(movie, auditorium, startTime, request.price());
		return ScreenResponse.from(screenRepository.save(screen));
	}

	// 상영 정보 삭제
	@Override
	@Transactional
	public void deleteScreen(Long screenId) {
		Screen screen = screenRepository.findById(screenId).orElseThrow(() -> NotFoundScreenException.EXCEPTION);
		screenRepository.delete(screen);
	}
}
