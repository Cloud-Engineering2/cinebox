package cinebox.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinebox.dto.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;

public record ScreenRequest(
		@NotNull(groups = CreateGroup.class)
		Long movieId,
		
		@NotNull(groups = CreateGroup.class)
		Long auditoriumId,
		
		@NotNull(groups = CreateGroup.class)
		LocalDateTime startTime,
		
		@NotNull(groups = CreateGroup.class)
		BigDecimal price
) {
	
}