package cinebox.dto.response;

import cinebox.common.enums.Gender;
import cinebox.common.enums.Role;
import cinebox.dto.request.UserRequest;
import cinebox.entity.User;

public record UserResponse(
		Long userId,
	    String identifier,
	    String email,
	    String name,
	    String phone,
	    Integer age,
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
                user.getGender(),
                user.getRole()
        );
	}
	
	public static UserResponse from(UserRequest user) {
        return new UserResponse(
                user.getUserId(),
                user.getIdentifier(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getAge(),
                user.getGender(),
                user.getRole()
        );
	}
}