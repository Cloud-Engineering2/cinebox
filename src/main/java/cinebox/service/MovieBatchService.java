package cinebox.service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cinebox.common.enums.MovieStatus;
import cinebox.dto.response.KmdbResponse;
import cinebox.dto.response.KobisMovieListResponse;
import cinebox.dto.response.KobisMovieListResponse.KobisMovieDto;
import cinebox.entity.Movie;
import cinebox.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieBatchService {
	private final MovieRepository movieRepository;
	private final RestTemplate restTemplate = createRestTemplate();

	private RestTemplate createRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML));
		restTemplate.getMessageConverters().add(0, converter);
		return restTemplate;
	}

	@Value("${kobis.api.key}")
	private String kobisApiKey;

	@Value("${kobis.api.url}")
	private String kobisApiUrl;
	
	@Value("${kmdb.api.key}")
	private String kmdbApiKey;
	
	@Value("${kmdb.api.url}")
	private String kmdbApiUrl;
	
	@Value("${itemPerPage:3}")
	private int itemPerPage;

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
	
	// 매주 월요일 자정에 영화 목록 업데이트
	@Scheduled(cron = "0 0 0 * * 1")
	public void fetchAndSaveMovies() {
		log.info("Starting movie batch job...");
		
		int openStartDt = LocalDate.now().getYear();
		List<KobisMovieDto> movieDtoList = fetchKobisMovieList(openStartDt);
		if (movieDtoList == null || movieDtoList.isEmpty()) {
			log.info("No movie data found from KOBIS API");
			return;
		}
		
		List<Movie> movies = movieDtoList.stream()
				.map(this::convertToMovie)
				.filter(movie -> movie != null)
				.collect(Collectors.toList());
		
		movieRepository.saveAll(movies);
		log.info("Movie batch job completed. Saved {} movies.", movies.size());
	}
	
	private List<KobisMovieDto> fetchKobisMovieList(int openStartDt) {
		String url = String.format("%s?key=%s&openStartDt=%d&itemPerPage=%d",
				kobisApiUrl, kobisApiKey, openStartDt, itemPerPage);
		KobisMovieListResponse response = restTemplate.getForObject(URI.create(url), KobisMovieListResponse.class);
		
		if (response == null || response.movieListResult() == null) {
			log.warn("No data returned from KOBIS API");
			return null;
		}
		
		return response.movieListResult().movieList();
	}
	
	private KmdbResponse fetchKmdbResponse(String title, String releaseDts, String releaseDte) {
		try {
			String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
			String kmdbUrl = String.format("%s&title=%s&releaseDts=%s&releaseDte=%s&ServiceKey=%s",
					kmdbApiUrl, encodedTitle, releaseDts, releaseDte, kmdbApiKey);
			
			log.info(kmdbUrl);
			
			return restTemplate.getForObject(URI.create(kmdbUrl), KmdbResponse.class);
		} catch (UnsupportedEncodingException e) {
			log.error("URL 인코딩 실패: {}", e.getMessage());
			return null;
		}
	}
	
	private Movie convertToMovie(KobisMovieDto dto) {
		String title = dto.movieNm();
		String openDt = dto.openDt();
		LocalDate releaseDate = LocalDate.parse(openDt, DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		String releaseDts = releaseDate.minusMonths(3).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String releaseDte = openDt;
		
		KmdbResponse kmdbResponse = fetchKmdbResponse(title, releaseDts, releaseDte);
		KmdbResponse.Result kmdbResult = extractResult(kmdbResponse);
		log.info("KMDB Response: {}", kmdbResponse);
		if (kmdbResult == null) {
			log.warn("No data returned from KMDB API");
			return null;
		}
		
		String posterImageUrl = extractPosterImageUrl(kmdbResult);
		String actors = extractActorNames(kmdbResult);
		String plot = extractPlotText(kmdbResult);
		Integer runtime = Integer.parseInt(extractRuntime(kmdbResult));
		String rating = extractRating(kmdbResult);
		
		return Movie.builder()
			.title(title)
			.plot(plot)
			.director(dto.extractDirectors())
			.actor(actors)
			.genre(dto.genreAlt())
			.posterImageUrl(posterImageUrl)
			.releaseDate(releaseDate)
			.runTime(runtime)
			.ratingGrade(rating)
			.status(MovieStatus.UNRELEASED)
			.build();
	}
	
	private KmdbResponse.Result extractResult(KmdbResponse kmdbResponse) {
		if (kmdbResponse == null || kmdbResponse.data() == null || kmdbResponse.data().isEmpty()) {
	        return null;
		}
		
		KmdbResponse.Data data = kmdbResponse.data().get(0);
	    if (data.result() == null || data.result().isEmpty()) {
	    	return null;
	    }
	    
	    return data.result().get(0);
	}
	private String extractPosterImageUrl(KmdbResponse.Result result) {
	    if (result == null || result.posters() == null || result.posters().isEmpty()) return null;
	    return result.posters().split("\\|")[0];
	}
	
	private String extractActorNames(KmdbResponse.Result result) {
	    if (result == null || result.actors() == null || result.actors().actor().isEmpty()) return null;
	    return result.actors().actor().stream()
	    		.map(KmdbResponse.Actor::actorNm)
	    		.collect(Collectors.joining(", "));
	}
	
	private String extractPlotText(KmdbResponse.Result result) {
	    if (result == null || result.plots() == null || result.plots().plot().isEmpty()) return null;
	    return result.plots().plot().stream()
        		.map(KmdbResponse.Plot::plotText)
        		.findFirst()
        		.orElse(null);
    }
	
	private String extractRuntime(KmdbResponse.Result result) {
	    if (result == null || result.runtime() == null || result.runtime().isEmpty()) return null;
	    return result.runtime();
	}
	
	private String extractRating(KmdbResponse.Result result) {
	    if (result == null || result.rating() == null || result.rating().isEmpty()) return null;
	    return result.rating();
	}
}
