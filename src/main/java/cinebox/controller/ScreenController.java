package cinebox.controller;

import cinebox.dto.ScreenRequest;
import cinebox.entity.Screen;
import cinebox.service.ScreenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/screen")
@RequiredArgsConstructor
public class ScreenController {
    private final ScreenService screenService;

    // 상영 정보 추가
    @PostMapping
    public ResponseEntity<Screen> createScreen(@RequestBody ScreenRequest request) {
        Screen screen = screenService.createScreen(request);
        return ResponseEntity.ok(screen);
    }

    // 상영 정보 수정
    @PutMapping("/{screenId}")
    public ResponseEntity<Screen> updateScreen(@PathVariable Long screenId, @RequestBody ScreenRequest request) {
        Screen updatedScreen = screenService.updateScreen(screenId, request);
        return ResponseEntity.ok(updatedScreen);
    }

    // 상영 정보 삭제
    @DeleteMapping("/{screenId}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long screenId) {
        screenService.deleteScreen(screenId);
        return ResponseEntity.noContent().build();
    }
}
