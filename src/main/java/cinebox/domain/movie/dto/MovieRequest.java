package cinebox.domain.movie.dto;

import java.time.LocalDate;

import cinebox.common.enums.MovieStatus;
import cinebox.common.validation.CreateGroup;
import cinebox.common.validation.UpdateGroup;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MovieRequest(
		@NotNull(message = "영화 제목을 입력해주세요.", groups = {CreateGroup.class, UpdateGroup.class})
		@NotEmpty(message = "영화 제목은 빈 값을 가질 수 없습니다.", groups = {CreateGroup.class, UpdateGroup.class})
		String title,

		@NotNull(message = "영화 줄거리를 입력해주세요.", groups = CreateGroup.class)
		String plot,

		@NotNull(message = "감독을 입력해주세요.", groups = CreateGroup.class)
		String director,

		@NotNull(message = "배우를 입력해주세요.", groups = CreateGroup.class)
		String actors,

		@NotNull(message = "장르를 입력해주세요.", groups = CreateGroup.class)
		String genre,

		@NotNull(message = "개봉일자를 입력해주세요.", groups = {CreateGroup.class, UpdateGroup.class})
		LocalDate releaseDate,

		@NotNull(message = "영화 러닝타임을 입력해주세요.", groups = CreateGroup.class)
		@Min(value = 1, message = "영화 러닝타임은 최소 1분 이상이어야 합니다.", groups = {CreateGroup.class, UpdateGroup.class})
		Integer runtime,

		String ratingGrade,
		MovieStatus status
) {

}
