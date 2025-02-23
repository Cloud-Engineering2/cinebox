package cinebox.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cinebox.common.enums.MovieStatus;
import cinebox.entity.Movie;
import cinebox.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieBatchService {
	private final MovieRepository movieRepository;
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${kobis.api.key}")
	private String kobisApiKey;

	@Value("${kobis.api.url}")
	private String kobisApiUrl;

	// 매 0시 당일 개봉일인 영화 개봉 상태 변경 (UPCOMING -> SHOWING) 
	@Scheduled(cron = "0 0 0 * * ?")
	public void updateMoviesToShowing() {
		LocalDate today = LocalDate.now();
		log.info("Starting updateMoviesToShowing for date: {}", today);
		
		List<Movie> movies = movieRepository.findByStatusAndReleaseDate(MovieStatus.UPCOMING, today);
		
		if (movies != null && !movies.isEmpty()) {
			movies.forEach(movie -> movie.updateMovieStatus(MovieStatus.SHOWING));
			movieRepository.saveAll(movies);
			log.info("Updated {} movies to SHOWING", movies.size());
		} else {
			log.info("No movies found to update for date: {}", today);
		}
	}
}
