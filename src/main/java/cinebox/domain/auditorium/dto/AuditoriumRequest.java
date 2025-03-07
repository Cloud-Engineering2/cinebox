package cinebox.domain.auditorium.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuditoriumRequest(
		@NotNull(message = "상영관 이름을 입력해주세요.")
		@NotEmpty(message = "상영관 이름은 빈 값을 가질 수 없습니다.")
		String name,
		
		@NotNull(message = "A부터 Z까지의 한 글자를 입력해주세요.")
		@Size(min = 1, max = 1, message = "A부터 Z까지의 한 글자만 허용됩니다.")
		@Pattern(regexp = "^[A-Z]$", message = "A부터 Z까지의 한 글자만 허용됩니다.")
		String row,
		
		@NotNull(message = "1부터 30까지의 값을 입력해주세요.")
		@Min(value = 1, message = "열은 1보다 작을 수 없습니다.")
		@Max(value = 30, message = "열은 최대 30까지 허용됩니다.")
		Integer column
) {

}
