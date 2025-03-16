package cinebox.domain.movie.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cinebox.common.validation.CreateGroup;
import cinebox.common.validation.UpdateGroup;
import cinebox.domain.movie.dto.MovieRequest;
import cinebox.domain.movie.dto.MovieResponse;
import cinebox.domain.movie.service.MovieService;
import cinebox.domain.movie.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
	private final MovieService movieService;
	private final S3Service s3Service;

	// 영화 등록 (생성)
	@PostMapping(consumes = {"multipart/form-data"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MovieResponse> registerMovie(
			@RequestPart("movie") @Validated(CreateGroup.class) MovieRequest request,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		log.info("영화 등록 요청: title={}, releaseDate={}", request.title(), request.releaseDate());
		String posterImageUrl = s3Service.uploadFile(request, image);
		MovieResponse response = movieService.registerMovie(request, posterImageUrl);
		log.info("영화 등록 완료: movieId={}", response.movieId());
		return ResponseEntity.ok(response);
	}
	
	// 영화 목록 조회 (정렬, 검색)
	@GetMapping
	public ResponseEntity<List<MovieResponse>> getAllMovies(
			@RequestParam(name = "sort", required = false) String sortBy,
			@RequestParam(name = "search", required = false) String searchText) {
		log.info("영화 목록 조회 요청: sortBy={}, searchText={}", sortBy, searchText);
		List<MovieResponse> responses = movieService.getAllMovies(sortBy, searchText);
		log.info("영화 목록 조회 완료, 결과 수: {}", responses.size());
		return ResponseEntity.ok(responses);
	}
	
	// 특정 영화 조회
	@GetMapping("/{movieId}")
	public ResponseEntity<MovieResponse> getMovie(@PathVariable("movieId") Long movieId) {
		log.info("특정 영화 조회 요청: movieId={}", movieId);
		MovieResponse response = movieService.getMovie(movieId);
		log.info("특정 영화 조회 완료: movieId={}", movieId);
		return ResponseEntity.ok(response);
	}

	// 영화 정보 수정
	@PutMapping(value = "/{movieId}", consumes = {"multipart/form-data"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<MovieResponse> updateMovie(
			@PathVariable("movieId") Long movieId,
			@RequestPart("movie") @Validated(UpdateGroup.class) MovieRequest request,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		log.info("영화 수정 요청: movieId={}, title={}", movieId, request.title());
		String posterImageUrl = s3Service.uploadFile(request, image);
		MovieResponse response = movieService.updateMovie(movieId, request, posterImageUrl);
		log.info("영화 수정 완료: movieId={}", movieId);
		return ResponseEntity.ok(response);
	}
	
	// 영화 삭제
	@DeleteMapping("/{movieId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteMovie(@PathVariable("movieId") Long movieId) {
		log.info("영화 삭제 요청: movieId={}", movieId);
		movieService.deleteMovie(movieId);
		log.info("영화 삭제 완료: movieId={}", movieId);
		return ResponseEntity.noContent().build();
	}
}
