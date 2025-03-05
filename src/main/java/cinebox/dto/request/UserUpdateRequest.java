package cinebox.dto.request;

import cinebox.common.enums.Gender;
import cinebox.common.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
		@Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
	    String password,
	    
	    @Email(message = "이메일 형식이 올바르지 않습니다.")
	    String email,
	    
	    @Size(min = 1, message = "이름을 입력해주세요.")
	    String name,
	    
	    @Pattern(regexp = "^01[0-9]-[0-9]{3,4}-[0-9]{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	    String phone,
	    
	    Integer age,
	    Gender gender,
	    Role role
) {

}
