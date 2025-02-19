package cinebox.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seat")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Seat extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seat_id")
	private Long seatId;

	@ManyToOne
	@JoinColumn(name = "auditorium_id", nullable = false)
	private Auditorium auditorium;

	@ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;  // Screen 엔티티와의 관계 추가
	
	@Column(nullable = false)
	private String seatNumber;

	@OneToMany(mappedBy = "seat")
	private List<BookingSeat> bookingSeats = new ArrayList<>();
}
