package cinebox.dto.request;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ScreenRequest {
    private Long movieId;
    private Long auditoriumId;
    private LocalDateTime startTime;
    private BigDecimal price;
}
