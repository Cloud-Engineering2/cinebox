package cinebox.dto.response;

import cinebox.common.enums.Gender;
import cinebox.common.enums.Role;
import cinebox.dto.request.UserRequest;
import cinebox.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    private Long userId;
    private String identifier;
    private String email;
    private String name;
    private String phone;
    private Integer age;
    private Gender gender;
    private Role role = Role.USER;

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
