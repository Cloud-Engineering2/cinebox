package cinebox.entity;

import cinebox.common.enums.BookingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking_seat")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingSeat extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_seat_id")
	private Long bookingSeatId;

	@ManyToOne
	@JoinColumn(name = "booking_id", nullable = false)
	private Booking booking;

	@ManyToOne
	@JoinColumn(name = "screen_id", nullable = false)
	private Screen screen;

	@ManyToOne
	@JoinColumn(name = "seat_id", nullable = false)
	private Seat seat;
	
	@Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status; // 예매 상태 필드 추가
	
}
