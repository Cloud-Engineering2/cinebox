package cinebox.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cinebox.common.enums.BookingStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "booking")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "booking_id")
	private Long bookingId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private LocalDateTime bookingDate;
	
	private BigDecimal totalPrice;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BookingStatus status;

	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingSeat> bookingSeats = new ArrayList<>();
	
	@OneToMany(mappedBy = "booking")
	private List<Payment> payments = new ArrayList<>();

	 // Set the totalPrice value
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    
    // 상태 변경을 위한 setter 메서드 추가
    public void setStatus(BookingStatus status) {
        this.status = status;
    }

	
}
