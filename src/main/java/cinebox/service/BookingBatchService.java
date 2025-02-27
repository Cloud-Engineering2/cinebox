package cinebox.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.enums.BookingStatus;
import cinebox.entity.Booking;
import cinebox.repository.BookingRepository;
import cinebox.repository.BookingSeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingBatchService {
	private final BookingRepository bookingRepository;
	private final BookingSeatRepository bookingSeatRepository;

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
			bookingSeatRepository.deleteByBooking(booking);
		}
		bookingRepository.deleteAll(expiredBookings);
		log.info("Deleted {} expired pending bookings", expiredBookings.size());
	}
}
