package cinebox.dto.response;

import java.time.LocalDate;

import cinebox.common.enums.Gender;
import cinebox.common.enums.Role;
import cinebox.entity.User;

public record UserResponse(
		Long userId,
	    String identifier,
	    String email,
	    String name,
	    String phone,
	    Integer age,
	    LocalDate birthDate,
	    Gender gender,
	    Role role
) {
	public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getIdentifier(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getAge(),
                user.getBirthDate(),
                user.getGender(),
                user.getRole()
        );
	}
}