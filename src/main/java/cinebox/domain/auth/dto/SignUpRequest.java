package cinebox.domain.auth.dto;

import java.time.LocalDate;

import cinebox.common.enums.Gender;
import cinebox.common.enums.PlatformType;
import cinebox.common.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;

public record SignUpRequest(
		@NotNull(message = "아이디는 4자 이상 20자 이하여야 합니다.")
		@Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하여야 합니다.")
		String identifier,
		
		@NotNull(groups = Default.class, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
		@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
		String password,
		
		@NotNull(message = "이메일 주소를 입력해주세요.")
		@Email(message = "이메일 형식이 올바르지 않습니다.")
		String email,
		
		@NotNull(message = "이름을 입력해주세요.")
		@Size(min = 1, message = "이름을 입력해주세요.")
		String name,
		
		@NotNull(message = "전화번호를 입력해주세요.")
		@Pattern(regexp = "^01[0-9]-[0-9]{3,4}-[0-9]{4}$", message = "전화번호 형식이 올바르지 않습니다.")
		String phone,
		
		@NotNull(message = "생년월일을 입력해주세요.")
		LocalDate birthDate,
		
		Gender gender,
		Role role,
		PlatformType platformType
) {

}