package cinebox.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.common.enums.PaymentStatus;
import cinebox.common.exception.payment.NotFoundPaymentException;
import cinebox.entity.Booking;
import cinebox.entity.Payment;
import cinebox.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingBatchService {
	private final BookingRepository bookingRepository;

	// 매 분마다 실행
	@Scheduled(cron = "0 * * * * ?")
	@Transactional
	public void deleteExpiredPendingBookings() {
		LocalDateTime cutoff = LocalDateTime.now().minusMinutes(10);
		List<Booking> expiredBookings = bookingRepository.findByStatusAndBookingDateBefore(BookingStatus.PENDING, cutoff);

		if (expiredBookings == null || expiredBookings.isEmpty()) {
			return;
		}

		for (Booking booking : expiredBookings) {
			booking.getBookingSeats().clear();
			booking.updateStatus(BookingStatus.CANCELED);
			Payment payment = booking.getPayments().stream()
					.max(Comparator.comparing(Payment::getCreatedAt))
					.orElseThrow(() -> NotFoundPaymentException.EXCEPTION);
			payment.updateStatus(PaymentStatus.FAILED);
		}
		
		bookingRepository.deleteAll(expiredBookings);
		log.info("Canceled {} expired pending bookings", expiredBookings.size());
	}
}
