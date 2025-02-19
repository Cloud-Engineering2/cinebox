package cinebox.dto.request;

import java.time.LocalDate;

public record MovieRequest(
		String title,
		String plot,
		String director,
		String actors,
		String genre,
		LocalDate releaseDate,
		Integer runtime,
		String ratingGrade
) {

}
