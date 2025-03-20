package cinebox.domain.screen.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.auditorium.NotFoundAuditoriumException;
import cinebox.common.exception.movie.NotFoundMovieException;
import cinebox.common.exception.screen.NotFoundScreenException;
import cinebox.common.exception.screen.ScreenTimeConflictException;
import cinebox.domain.auditorium.entity.Auditorium;
import cinebox.domain.auditorium.repository.AuditoriumRepository;
import cinebox.domain.movie.entity.Movie;
import cinebox.domain.movie.repository.MovieRepository;
import cinebox.domain.screen.dto.AuditoriumScreenResponse;
import cinebox.domain.screen.dto.DateScreenResponse;
import cinebox.domain.screen.dto.ScreenRequest;
import cinebox.domain.screen.dto.ScreenResponse;
import cinebox.domain.screen.entity.Screen;
import cinebox.domain.screen.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		log.info("상영 정보 생성 요청: movieId={}, auditoriumId={}, startTime={}", request.movieId(), request.auditoriumId(), request.startTime());
		Movie movie = movieRepository.findById(request.movieId())
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);

		Auditorium auditorium = auditoriumRepository.findById(request.auditoriumId())
				.orElseThrow(() -> NotFoundAuditoriumException.EXCEPTION);

		LocalDateTime endTime = request.startTime().plusMinutes(movie.getRunTime() + 10);

		List<Screen> overlappingScreens = screenRepository
				.findByAuditoriumAndStartTimeLessThanAndEndTimeGreaterThan(auditorium, endTime, request.startTime());

		if (!overlappingScreens.isEmpty()) {
			log.error("상영 시간 겹침 감지: auditoriumId={}, startTime={}", auditorium.getAuditoriumId(), request.startTime());
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
		log.info("상영 정보 생성 완료: screenId={}", screen.getScreenId());
		return ScreenResponse.from(screen);
	}

	// 상영 정보 수정
	@Override
	@Transactional
	public ScreenResponse updateScreen(Long screenId, ScreenRequest request) {
		log.info("상영 정보 수정 요청: screenId={}", screenId);
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
			log.error("상영 정보 수정 실패 - 시간 겹침: screenId={}, newStartTime={}", screenId, startTime);
			throw ScreenTimeConflictException.EXCEPTION;
		}

		screen.updateScreen(movie, auditorium, startTime, request.price());
		Screen saved = screenRepository.save(screen);
		log.info("상영 정보 수정 완료: screenId={}", saved.getScreenId());
		return ScreenResponse.from(saved);
	}

	// 상영 정보 삭제
	@Override
	@Transactional
	public void deleteScreen(Long screenId) {
		log.info("상영 정보 삭제 요청: screenId={}", screenId);
		Screen screen = screenRepository.findById(screenId)
				.orElseThrow(() -> NotFoundScreenException.EXCEPTION);
		screenRepository.delete(screen);
		log.info("상영 정보 삭제 완료: screenId={}", screenId);
	}

	// 특정 영화 상영 날짜 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<LocalDate> getAvailableDatesForMovie(Long movieId) {
		log.info("특정 영화 상영 날짜 목록 조회 요청: movieId={}", movieId);
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> NotFoundMovieException.EXCEPTION);

		List<Screen> screens = movie.getScreens();

		Set<LocalDate> dateSet = screens.stream()
				.map(screen -> screen.getStartTime().toLocalDate())
				.collect(Collectors.toSet());

		List<LocalDate> dates = dateSet.stream().sorted().collect(Collectors.toList());
		log.info("특정 영화 상영 날짜 목록 조회 완료: movieId={}, 날짜 수={}", movieId, dates.size());
		return dates;
	}

	// 날짜별 상영 정보 조회
	@Override
	@Transactional(readOnly = true)
	public List<AuditoriumScreenResponse> getScreensByDate(Long movieId, LocalDate date) {
		log.info("날짜별 상영 정보 조회 요청: movieId={}, date={}", movieId, date);
		movieRepository.findById(movieId).orElseThrow(() -> NotFoundMovieException.EXCEPTION);

		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

		List<Screen> screens = screenRepository
				.findByMovie_MovieIdAndStartTimeBetweenOrderByStartTimeAsc(movieId, startOfDay, endOfDay);

		List<ScreenResponse> screenResponses = screens.stream()
				.map(ScreenResponse::from)
				.collect(Collectors.toList());
		
		Map<Long, List<ScreenResponse>> grouped = screenResponses.stream()
				.collect(Collectors.groupingBy(ScreenResponse::auditoriumId));
		
		List<AuditoriumScreenResponse> responses = grouped.values().stream()
				.map(AuditoriumScreenResponse::from)
				.collect(Collectors.toList());
		log.info("날짜별 상영 정보 조회 완료: movieId={}, date={}, 그룹 수={}", movieId, date, responses.size());
		return responses;
	}

	// 모든 상영 정보 조회 (날짜 내림차순)
	@Override
	@Transactional(readOnly = true)
	public List<DateScreenResponse> getAllScreens() {
		log.info("전체 상영 정보 조회 요청");
		List<Screen> screens = screenRepository.findAll();
		List<DateScreenResponse> responses = groupScreensByDateDesc(screens);
		log.info("전체 상영 정보 조회 완료, 그룹 수={}", responses.size());
		return responses;
	}

	// 상영될 모든 상영 정보 조회 (날짜 오름차순)
	@Override
	@Transactional(readOnly = true)
	public List<DateScreenResponse> getUpcomingScreens() {
		LocalDateTime now = LocalDateTime.now();
		log.info("상영 예정 상영 정보 조회 요청, 현재시간={}", now);
		List<Screen> screens = screenRepository.findByStartTimeAfter(now);

		List<DateScreenResponse> responses = groupScreensByDateAsc(screens);
		log.info("상영 예정 상영 정보 조회 완료, 그룹 수={}", responses.size());
		return responses;
	}

	// 영화별 상영 정보 목록 조회
	@Override
	@Transactional(readOnly = true)
	public List<DateScreenResponse> getScreensByMovie(Long movieId) {
		log.info("영화별 상영 정보 조회 요청: movieId={}", movieId);
		Movie movie = movieRepository.findById(movieId).orElseThrow(() -> NotFoundMovieException.EXCEPTION);

		List<Screen> screens = screenRepository.findByMovie(movie);

		List<DateScreenResponse> responses = groupScreensByDateAsc(screens);
		log.info("영화별 상영 정보 조회 완료: movieId={}, 그룹 수={}", movieId, responses.size());
		return responses;
	}

	// 날짜별 상영 정보 그룹화 (내림차순)
	private List<DateScreenResponse> groupScreensByDateDesc(List<Screen> screens) {
		List<ScreenResponse> screenResponses = screens.stream()
				.map(ScreenResponse::from)
				.collect(Collectors.toList());
		
		Map<LocalDate, List<ScreenResponse>> grouped = screenResponses.stream()
				.collect(Collectors.groupingBy(response -> response.startTime().toLocalDate()));
		
		return grouped.entrySet().stream()
				.map(entry -> DateScreenResponse.from(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}
	
	// 날짜별 상영 정보 그룹화 (오름차순)
	private List<DateScreenResponse> groupScreensByDateAsc(List<Screen> screens) {
		List<ScreenResponse> screenResponses = screens.stream()
				.map(ScreenResponse::from)
				.collect(Collectors.toList());
		
		Map<LocalDate, List<ScreenResponse>> grouped = new TreeMap<>(screenResponses.stream()
				.collect(Collectors.groupingBy(response -> response.startTime().toLocalDate())));
		
		return grouped.entrySet().stream()
				.map(entry -> DateScreenResponse.from(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}
}
