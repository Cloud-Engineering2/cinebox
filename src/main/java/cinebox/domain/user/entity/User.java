package cinebox.domain.user.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import cinebox.common.entity.BaseTimeEntity;
import cinebox.common.enums.Gender;
import cinebox.common.enums.PlatformType;
import cinebox.common.enums.Role;
import cinebox.domain.auth.dto.SignUpRequest;
import cinebox.domain.booking.entity.Booking;
import cinebox.domain.like.entity.MovieLike;
import cinebox.domain.review.entity.Review;
import cinebox.domain.user.dto.UserUpdateRequest;
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
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
		name = "user",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"email", "platform_type"}),
				@UniqueConstraint(columnNames = {"phone", "platform_type"})
		})
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
	private String identifier;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String phone;

	@Column(name = "birth_date")
	private LocalDate birthDate;
	
	@Transient
	public Integer getAge() {
		if (this.birthDate == null) return null;
		LocalDate today = LocalDate.now();
		int age = today.getYear() - this.birthDate.getYear();
		if (today.isBefore(this.birthDate.withYear(today.getYear()))) {
			age --;
		}
		return age;
	}

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@Column(nullable = false, name = "platform_type")
	@Enumerated(EnumType.STRING)
	private PlatformType platformType = PlatformType.LOCAL;

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

	public void updateUser(UserUpdateRequest request, String encodedPassword) {
		this.email = request.email() != null ? request.email() : this.email;
		this.password = encodedPassword != null ? encodedPassword : this.password;
		this.name = request.name() != null ? request.name() : this.name;
		this.phone = request.phone() != null ? request.phone() : this.phone;
		this.birthDate = request.birthDate() != null ? request.birthDate() : this.birthDate;
	}
	
	public void updateUserRole(Role role) {
		this.role = role != null ? role : this.role;
	}

	public void restoreUser() {
		this.isDeleted = false;
	}

	public static User createUser(SignUpRequest request, String encodedPassword, PlatformType platformType) {
		return User.builder()
				.identifier(request.identifier())
				.password(encodedPassword)
				.email(request.email())
				.name(request.name())
				.phone(request.phone())
				.birthDate(request.birthDate())
				.gender(request.gender())
				.role(request.role())
				.platformType(platformType)
				.build();
	}
}
