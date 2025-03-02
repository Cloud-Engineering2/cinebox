package cinebox.dto.request;

import cinebox.dto.validation.CreateGroup;
import cinebox.dto.validation.UpdateGroup;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
		@NotNull(groups = CreateGroup.class, message = "평점을 입력하세요")
		@Min(groups = {CreateGroup.class, UpdateGroup.class}, value = 1, message = "0점 이상의 점수를 입력하세요.")
		@Max(groups = {CreateGroup.class, UpdateGroup.class}, value = 5, message = "5점 이하의 점수를 입력하세요.")
		Integer rating,
		
		@NotNull(groups = CreateGroup.class, message = "리뷰 내용을 입력하세요.")
		@NotEmpty(groups = {CreateGroup.class, UpdateGroup.class}, message = "리뷰 내용을 입력하세요.")
		String content
) {

}