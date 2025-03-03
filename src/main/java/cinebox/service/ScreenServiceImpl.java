package cinebox.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.auditorium.NotFoundAuditoriumException;
import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.exception.screen.NotFoundScreenException;
import cinebox.common.exception.screen.ScreenTimeConflictException;
import cinebox.dto.request.ScreenRequest;
import cinebox.dto.response.AuditoriumScreenResponse;
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

		LocalDateTime endTime = request.startTime().plusMinutes(movie.getRunTime() + 10);

		List<Screen> overlappingScreens = screenRepository
				.findByAuditoriumAndStartTimeLessThanAndEndTimeGreaterThan(auditorium, endTime, request.startTime());

		if (!overlappingScreens.isEmpty()) {
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
		Screen screen = screenRepository.findById(screenId)
				.orElseThrow(() -> NotFoundScreenException.EXCEPTION);

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
		Screen saved = screenRepository.save(screen);
		return ScreenResponse.from(saved);
	}

	// 상영 정보 삭제
	@Override
	@Transactional
	public void deleteScreen(Long screenId) {
		Screen screen = screenRepository.findById(screenId)
				.orElseThrow(() -> NotFoundScreenException.EXCEPTION);
		screenRepository.delete(screen);
	}

	// 특정 영화 상영 날짜 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<LocalDate> getAvailableDatesForMovie(Long movieId) {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);

		List<Screen> screens = movie.getScreens();

		Set<LocalDate> dateSet = screens.stream()
				.map(screen -> screen.getStartTime().toLocalDate())
				.collect(Collectors.toSet());

		return dateSet.stream().sorted().collect(Collectors.toList());
	}

	// 날짜별 상영 정보 조회
	@Override
	@Transactional(readOnly = true)
	public List<AuditoriumScreenResponse> getScreensByDate(Long movieId, LocalDate date) {
		movieRepository.findById(movieId).orElseThrow(() -> NotFoundMovieException.EXCEPTION);

		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

		List<Screen> screens = screenRepository
				.findByMovie_MovieIdAndStartTimeBetweenOrderByStartTimeAsc(movieId, startOfDay, endOfDay);

		List<ScreenResponse> screenResponses =  screens.stream()
				.map(ScreenResponse::from)
				.collect(Collectors.toList());
		
		Map<Long, List<ScreenResponse>> grouped = screenResponses.stream()
				.collect(Collectors.groupingBy(ScreenResponse::auditoriumId));
		
		return grouped.values().stream()
				.map(AuditoriumScreenResponse::from)
				.collect(Collectors.toList());
	}
}
