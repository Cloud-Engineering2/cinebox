package cinebox.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.security.crypto.password.PasswordEncoder;

import cinebox.common.enums.Gender;
import cinebox.common.enums.Role;
import cinebox.dto.request.UserRequest;
import cinebox.dto.request.UserUpdateRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE user_id = ?") // 삭제 시 논리 삭제 처리
@SQLRestriction("is_deleted = false")	// 조회 시 is_deleted가 false인 데이터만 불러옴
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Column(nullable = false, unique = true)
	@Pattern(regexp = "^.{4,20}$", message = "아이디는 4자 이상 20자 이하이어야 합니다.")
	private String identifier;

	@Column(nullable = false, unique = true)
	@Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", 
    		 message = "이메일 형식이 올바르지 않습니다.")
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	@Pattern(regexp = "^01[0-9]-[0-9]{3,4}-[0-9]{4}$", message = "전화번호 형식이 올바르지 않습니다.")
	private String phone;

	private Integer age;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToMany(mappedBy = "user")
	private List<Booking> bookings = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Review> reviews = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MovieLike> movieLikes = new ArrayList<>();
	
    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDeleted = false;

	public User(String identifier, String password, Role role) {
		this.identifier = identifier;
		this.password = password;
		this.role = role;
	}

	public static User of(UserRequest userDTO) {
        return new User(
        		userDTO.getUserId(),
        		userDTO.getIdentifier(),
        		userDTO.getEmail(),
        		userDTO.getPassword(),
        		userDTO.getName(),
        		userDTO.getPhone(),
        		userDTO.getAge(),
        		userDTO.getGender(),
        		userDTO.getRole(),
        		null,
        		null,
        		null,
        	    false
    		);
	}

	public void updateUser(UserUpdateRequest request, String encodedPassword) {
		this.email = request.email() != null ? request.email() : this.email;
		this.password = encodedPassword != null ? encodedPassword : this.password;
		this.name = request.name() != null ? request.name() : this.name;
		this.phone = request.phone() != null ? request.phone() : this.phone;
		this.age = request.age() != null ? request.age() : this.age;
	}
	
	public void updateUserRole(UserUpdateRequest request) {
		this.role = request.role() != null ? request.role() : this.role;
	}

	public void restoreUser() {
		this.isDeleted = false;
	}
}
