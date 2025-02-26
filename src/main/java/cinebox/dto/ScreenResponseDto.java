package cinebox.dto;

import cinebox.entity.Screen;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ScreenResponseDto {
    private Long screenId;
    private Long movieId;
    private Long auditoriumId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;

    public ScreenResponseDto(Screen screen) {
        this.screenId = screen.getScreenId();
        this.movieId = screen.getMovie().getMovieId();
        this.auditoriumId = screen.getAuditorium().getAuditoriumId();
        this.startTime = screen.getStartTime();
        this.endTime = screen.getEndTime();
        this.price = screen.getPrice();
    }
}
