package cinebox.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.MovieRequest;
import cinebox.dto.response.MovieResponse;
import cinebox.dto.response.ScreenResponse;
import cinebox.dto.validation.CreateGroup;
import cinebox.service.MovieService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
	private final MovieService movieService;

	// TODO: s3 연동 및 MultiPart 적용
	// 영화 등록 (생성)
	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MovieResponse> registerMovie(@RequestBody @Validated(CreateGroup.class) MovieRequest request) {
		return ResponseEntity.ok(movieService.registerMovie(request));
	}
	
	// 영화 목록 조회 (정렬, 검색)
	@GetMapping
	public ResponseEntity<List<MovieResponse>> getAllMovies(
			@RequestParam(name = "sort", required = false) String sortBy,
			@RequestParam(name = "search", required = false) String searchText) {
		return ResponseEntity.ok(movieService.getAllMovies(sortBy, searchText));
	}
	
	// 특정 영화 조회
	@GetMapping("/{movieId}")
	public ResponseEntity<MovieResponse> getMovie(@PathVariable("movieId") Long movieId) {
		return ResponseEntity.ok(movieService.getMovie(movieId));
	}
	
	// 특정 영화 상영 날짜 목록 조회
	@GetMapping("/{movieId}/dates")
	public ResponseEntity<List<LocalDate>> getAvailableDatesForMovie(@PathVariable("movieId") Long movieId) {
		List<LocalDate> dates = movieService.getAvailableDatesForMovie(movieId);
		return ResponseEntity.ok(dates);
	}
	
	//TODO: s3 연동하여 이미지 처리
	// 영화 정보 수정
	@PutMapping("/{movieId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MovieResponse> updateMovie(
			@PathVariable("movieId") Long movieId,
			@RequestBody MovieRequest request) {
		return ResponseEntity.ok(movieService.updateMovie(movieId, request));
	}
	
	// 영화 삭제
	@DeleteMapping("/{movieId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteMovie(@PathVariable("movieId") Long movieId) {
		movieService.deleteMovie(movieId);
		return ResponseEntity.noContent().build();
	}
    
    // 특정 영화의 날짜별 상영 정보 조회
    @GetMapping("/{movieId}/screens")
    public ResponseEntity<List<ScreenResponse>> getScreensByDate(
    		@PathVariable("movieId") Long movieId,
    		@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    	List<ScreenResponse> responses = movieService.getScreensByDate(movieId, date);
		return ResponseEntity.ok(responses);
	}
}
