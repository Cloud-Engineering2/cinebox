package cinebox.dto.request;

import cinebox.dto.validation.CreateGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
		@NotNull(groups = CreateGroup.class, message = "평점을 입력해주세요")
		@Min(value = 0, message = "0점 미만의 점수를 줄 수 없습니다.")
		@Max(value = 5, message = "5점 초과의 점수를 줄 수 없습니다.")
		int rating,
		
		@NotNull(groups = CreateGroup.class, message = "리뷰 내용을 입력해주세요.")
		String content
) {

}