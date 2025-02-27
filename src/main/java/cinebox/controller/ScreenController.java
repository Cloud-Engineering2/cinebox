package cinebox.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cinebox.dto.request.ScreenRequest;
import cinebox.dto.response.ScreenResponse;
import cinebox.dto.validation.CreateGroup;
import cinebox.service.ScreenService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/screen")
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenService screenService;

    // 상영 정보 추가
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ScreenResponse> createScreen(@RequestBody @Validated(CreateGroup.class) ScreenRequest request) {
        ScreenResponse responseDto = screenService.createScreen(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 상영 정보 수정
    @PutMapping("/{screenId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ScreenResponse> updateScreen(
            @PathVariable("screenId") Long screenId,
            @RequestBody ScreenRequest request) {
        ScreenResponse updatedScreen = screenService.updateScreen(screenId, request);
        return ResponseEntity.ok(updatedScreen);
    }

    // 상영 정보 삭제
    @DeleteMapping("/{screenId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteScreen(@PathVariable("screenId") Long screenId) {
        screenService.deleteScreen(screenId);
		return ResponseEntity.noContent().build();
	}
}
