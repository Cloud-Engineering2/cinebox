package cinebox.dto;

import java.time.LocalDateTime;

import cinebox.common.enums.Gender;
import cinebox.common.enums.Role;
import cinebox.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String identifier;
    private String password;
    private String email;
    private String name;
    private String phone;
    private Integer age;
    private Gender gender;
    private Role role = Role.USER;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

	public static UserDTO from(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getIdentifier(),
                user.getPassword(),
                user.getEmail(),
                user.getName(),
                user.getPhone(),
                user.getAge(),
                user.getGender(),
                user.getRole(),
                null,
                null
        );
	}

}
