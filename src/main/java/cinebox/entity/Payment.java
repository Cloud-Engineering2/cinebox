package cinebox.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import cinebox.common.enums.PaymentMethod;
import cinebox.common.enums.PaymentStatus;
import cinebox.dto.request.PaymentRequest;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long paymentId;

	//@ManyToOne
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) 
	@JoinColumn(name = "booking_id", nullable = false)
	private Booking booking;

	private BigDecimal amount;

	private PaymentMethod method;

	@Enumerated(EnumType.STRING)
	private PaymentStatus status;

	@Column(name = "paid_at")
	private LocalDateTime paidAt;

	@Column(name = "canceled_at")
	private LocalDateTime canceledAt;
	
	public static Payment createPayment(Booking booking) {
		return Payment.builder()
				.booking(booking)
				.amount(booking.getTotalPrice())
				.status(PaymentStatus.REQUESTED)
				.build();
	}

	public void updateStatus(PaymentStatus status) {
		this.status = status;
	}
	
	public void processPayment(PaymentRequest request) {
		this.method = request.method();
		this.status = PaymentStatus.COMPLETED;
		this.paidAt = LocalDateTime.now();
	}
}
