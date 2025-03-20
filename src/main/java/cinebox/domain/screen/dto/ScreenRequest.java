package cinebox.domain.screen.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinebox.common.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;

public record ScreenRequest(
		@NotNull(groups = CreateGroup.class, message = "Movie Id를 입력해주세요.")
		Long movieId,
		
		@NotNull(groups = CreateGroup.class, message = "Auditorium Id를 입력해주세요.")
		Long auditoriumId,
		
		@NotNull(groups = CreateGroup.class, message = "영화 시작 시간을 입력해주세요.")
		LocalDateTime startTime,
		
		@NotNull(groups = CreateGroup.class, message = "예매 가격을 입력해주세요.")
		BigDecimal price
) {
	
}