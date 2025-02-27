package cinebox.entity;

import cinebox.dto.BookingSeatDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "booking_seat")
@Getter
@NoArgsConstructor
public class BookingSeat extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_seat_id")
	private Long bookingSeatId;

	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "booking_id", nullable = false)
	private Booking booking;

	@ManyToOne
	@JoinColumn(name = "screen_id", nullable = false)
	private Screen screen;

	@ManyToOne
	@JoinColumn(name = "seat_id", nullable = false)
	private Seat seat;
	
	// Booking, Screen, Seat을 받는 생성자 추가
    public BookingSeat(Booking booking, Screen screen, Seat seat) {
        this.booking = booking;
        this.screen = screen;
        this.seat = seat;
    }

    public static BookingSeat fromDTO(BookingSeatDTO dto) {
        // 생성자를 사용하여 객체 초기화
        return new BookingSeat(dto.getBooking(), dto.getScreen(), dto.getSeat());
    }
}
