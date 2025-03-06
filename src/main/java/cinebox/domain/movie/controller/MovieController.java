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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cinebox.common.validation.CreateGroup;
import cinebox.domain.movie.dto.MovieRequest;
import cinebox.domain.movie.dto.MovieResponse;
import cinebox.domain.movie.service.MovieService;
import cinebox.domain.movie.service.S3Service;
import lombok.RequiredArgsConstructor;

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
			@RequestPart("image") MultipartFile image) {
		String posterImageUrl = s3Service.uploadFile(request, image);
		MovieResponse response = movieService.registerMovie(request, posterImageUrl);
		return ResponseEntity.ok(response);
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
}
