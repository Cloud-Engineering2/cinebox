package cinebox.entity;

import java.util.ArrayList;
import java.util.List;

import cinebox.common.enums.Gender;
import cinebox.common.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
public class User extends BaseTimeEntity{
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
}
