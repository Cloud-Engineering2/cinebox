package cinebox.dto.request;

import cinebox.common.enums.Gender;
import cinebox.common.enums.Role;
import cinebox.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private Long userId;
    private String identifier;
    private String password;
    private String email;
    private String name;
    private String phone;
    private Integer age;
    private Gender gender;
    private Role role = Role.USER;

	public static UserRequest from(User user) {
        return new UserRequest(
                user.getUserId(),
                user.getIdentifier(),
                user.getPassword(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getAge(),
                user.getGender(),
                user.getRole()
        );
	}
}
