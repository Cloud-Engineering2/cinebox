package cinebox.dto.request;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinebox.dto.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;

@Getter
public class ScreenRequest {
	@NotNull(groups = CreateGroup.class)
	private Long movieId;

	@NotNull(groups = CreateGroup.class)
	private Long auditoriumId;

	@NotNull(groups = CreateGroup.class)
	private LocalDateTime startTime;

	@NotNull(groups = CreateGroup.class)
	private BigDecimal price;
}
