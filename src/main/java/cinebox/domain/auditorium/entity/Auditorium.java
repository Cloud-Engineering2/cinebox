package cinebox.domain.auditorium.entity;

import java.util.ArrayList;
import java.util.List;

import cinebox.common.entity.BaseTimeEntity;
import cinebox.domain.screen.entity.Screen;
import cinebox.domain.seat.entity.Seat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auditorium")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Auditorium extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "auditorium_id")
	private Long auditoriumId;

	@Column(nullable = false)
	private String name;

	@Transient
	public Integer getCapacity() {
		return seats.size();
	}

	@OneToMany(mappedBy = "auditorium", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Seat> seats = new ArrayList<>();

	@OneToMany(mappedBy = "auditorium")
	private List<Screen> screens = new ArrayList<>();
}
