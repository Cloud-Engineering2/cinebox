package cinebox.dto.request;

import java.time.LocalDate;

import cinebox.common.enums.MovieStatus;
import cinebox.dto.validation.CreateGroup;
import jakarta.validation.constraints.NotNull;

public record MovieRequest(
		@NotNull(groups = CreateGroup.class)
		String title,
		
		@NotNull(groups = CreateGroup.class)
		String plot,
		
		@NotNull(groups = CreateGroup.class)
		String director,
		
		@NotNull(groups = CreateGroup.class)
		String actors,
		
		@NotNull(groups = CreateGroup.class)
		String genre,
		
		@NotNull(groups = CreateGroup.class)
		LocalDate releaseDate,
		
		@NotNull(groups = CreateGroup.class)
		Integer runtime,
		
		@NotNull(groups = CreateGroup.class)
		String ratingGrade,
		
		MovieStatus status
) {

}
