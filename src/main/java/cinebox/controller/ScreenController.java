package cinebox.controller;

import cinebox.dto.ScreenRequest;
import cinebox.dto.ScreenResponseDto;
import cinebox.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/screen")
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenService screenService;

    // 상영 정보 추가
    @PostMapping
    public ResponseEntity<ScreenResponseDto> createScreen(@RequestBody ScreenRequest request) {
        System.out.println("상영 정보 추가 요청 수신: " + request.toString());
        ScreenResponseDto responseDto = screenService.createScreen(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 상영 정보 수정
    @PutMapping("/{screenId}")
    public ResponseEntity<ScreenResponseDto> updateScreen(
            @PathVariable Long screenId,
            @RequestBody ScreenRequest request) {
        ScreenResponseDto updatedScreen = screenService.updateScreen(screenId, request);
        return ResponseEntity.ok(updatedScreen);
    }

    // 상영 정보 삭제
    @DeleteMapping("/{screenId}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long screenId) {
        screenService.deleteScreen(screenId);
        return ResponseEntity.noContent().build();
    }
}
