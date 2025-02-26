package cinebox.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ScreenRequest {
    private Long movieId;
    private Long auditoriumId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
}
