package cinebox.dto.request;

import cinebox.common.enums.Gender;
import cinebox.common.enums.Role;

public record UserUpdateRequest(
	    String password,
	    String email,
	    String name,
	    String phone,
	    Integer age,
	    Gender gender,
	    Role role
) {

}
