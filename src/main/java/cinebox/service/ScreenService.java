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
public class ScreenService {
	private final ScreenRepository screenRepository;
	private final MovieRepository movieRepository;
	private final AuditoriumRepository auditoriumRepository;

	// 상영 정보 추가
	@Transactional
	public ScreenResponse createScreen(ScreenRequest request) {
		Movie movie = movieRepository.findById(request.getMovieId())
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);

		Auditorium auditorium = auditoriumRepository.findById(request.getAuditoriumId())
				.orElseThrow(() -> NotFoundAuditoriumException.EXCEPTION);

		LocalDateTime endTime = request.getStartTime().plusMinutes(movie.getRunTime() + 10); // 🎯 endTime 자동 계산
		
		List<Screen> overlappingScreens = screenRepository
				.findByAuditoriumAndStartTimeLessThanAndEndTimeGreaterThan(auditorium, endTime, request.getStartTime());
		if(!overlappingScreens.isEmpty()) {
			throw ScreenTimeConflictException.EXCEPTION;
		}

		Screen screen = Screen.builder()
				.movie(movie)
				.auditorium(auditorium)
				.startTime(request.getStartTime())
				.endTime(endTime)
				.price(request.getPrice())
				.build();

		screenRepository.save(screen);
		return new ScreenResponse(screen);
	}

	// 상영 정보 수정
	@Transactional
	public ScreenResponse updateScreen(Long screenId, ScreenRequest request) {
		Screen screen = screenRepository.findById(screenId).orElseThrow(() -> NotFoundScreenException.EXCEPTION);

		Movie movie = request.getMovieId() != null
				? movieRepository.findById(request.getMovieId())
						.orElseThrow(() -> NotFoundMovieException.EXCEPTION)
				: screen.getMovie();
		
		Auditorium auditorium = request.getAuditoriumId() != null 
	            ? auditoriumRepository.findById(request.getAuditoriumId())
						.orElseThrow(() -> NotFoundAuditoriumException.EXCEPTION)
				: screen.getAuditorium();
		
		LocalDateTime startTime = request.getStartTime() != null ? request.getStartTime() : screen.getStartTime();
		LocalDateTime endTime = startTime.plusMinutes(movie.getRunTime() + 10);
		
		List<Screen> overlappingScreens = screenRepository
				.findByAuditoriumAndScreenIdNotAndStartTimeLessThanAndEndTimeGreaterThan(auditorium, screenId, endTime, startTime);
        if (!overlappingScreens.isEmpty()) {
            throw ScreenTimeConflictException.EXCEPTION;
        }

		screen.updateScreen(movie, auditorium, startTime, request.getPrice());
		return new ScreenResponse(screenRepository.save(screen));
	}

	// 상영 정보 삭제
	@Transactional
	public void deleteScreen(Long screenId) {
		Screen screen = screenRepository.findById(screenId).orElseThrow(() -> NotFoundScreenException.EXCEPTION);
		screenRepository.delete(screen);
	}
}
