package cinebox.domain.screen.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

import cinebox.common.validation.CreateGroup;
import cinebox.domain.screen.dto.AuditoriumScreenResponse;
import cinebox.domain.screen.dto.DateScreenResponse;
import cinebox.domain.screen.dto.ScreenRequest;
import cinebox.domain.screen.dto.ScreenResponse;
import cinebox.domain.screen.service.ScreenService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenService screenService;

    // 상영 정보 추가
    @PostMapping("/screens")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ScreenResponse> createScreen(
    		@RequestBody @Validated(CreateGroup.class) ScreenRequest request) {
        ScreenResponse responseDto = screenService.createScreen(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 상영 정보 수정
    @PutMapping("/screens/{screenId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ScreenResponse> updateScreen(
            @PathVariable("screenId") Long screenId,
            @RequestBody ScreenRequest request) {
        ScreenResponse updatedScreen = screenService.updateScreen(screenId, request);
        return ResponseEntity.ok(updatedScreen);
    }

    // 상영 정보 삭제
    @DeleteMapping("/screens/{screenId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteScreen(@PathVariable("screenId") Long screenId) {
        screenService.deleteScreen(screenId);
		return ResponseEntity.noContent().build();
	}
	
	// 특정 영화 상영 날짜 목록 조회
	@GetMapping("/movies/{movieId}/dates")
	public ResponseEntity<List<LocalDate>> getAvailableDatesForMovie(@PathVariable("movieId") Long movieId) {
		List<LocalDate> dates = screenService.getAvailableDatesForMovie(movieId);
		return ResponseEntity.ok(dates);
	}
    
    // 특정 영화의 날짜별 상영 정보 조회
    @GetMapping("/movies/{movieId}/screens/date")
    public ResponseEntity<List<AuditoriumScreenResponse>> getScreensByDate(
    		@PathVariable("movieId") Long movieId,
    		@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    	List<AuditoriumScreenResponse> responses = screenService.getScreensByDate(movieId, date);
		return ResponseEntity.ok(responses);
	}
    
    // 모든 상영 정보 조회
    @GetMapping("/screens")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<DateScreenResponse>> getAllScreens() {
    	List<DateScreenResponse> responses = screenService.getAllScreens();
    	return ResponseEntity.ok(responses);
    }
    
    // 상영될 모든 상영 정보 조회
    @GetMapping("/screens/upcoming")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<DateScreenResponse>> getUpcomingScreens() {
    	List<DateScreenResponse> responses = screenService.getUpcomingScreens();
    	return ResponseEntity.ok(responses);
    }
    
    // 영화별 상영 정보 목록 조회
    @GetMapping("/movies/{movieId}/screens")
    public ResponseEntity<List<DateScreenResponse>> getScreensByMovie(
    		@PathVariable("movieId") Long movieId) {
    	List<DateScreenResponse> responses = screenService.getScreensByMovie(movieId);
    	return ResponseEntity.ok(responses);
    }
}
