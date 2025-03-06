package cinebox.domain.seat.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cinebox.common.exception.screen.NotFoundScreenException;
import cinebox.domain.screen.entity.Screen;
import cinebox.domain.screen.repository.ScreenRepository;
import cinebox.domain.seat.dto.SeatResponse;
import cinebox.domain.seat.entity.Seat;
import cinebox.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
	private final ScreenRepository screenRepository;
	private final SeatRepository seatRepository;

	// 좌석 조회
	@Override
	public List<SeatResponse> getSeats(Long screenId) {
		Screen screen = screenRepository.findById(screenId)
				.orElseThrow(() -> NotFoundScreenException.EXCEPTION);
		
		Long auditoriumId = screen.getAuditorium().getAuditoriumId();
//		Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
//				.orElseThrow(() -> NotFoundAuditoriumException.EXCEPTION);
//		List<Seat> seats = auditorium.getSeats();
		List<Seat> seats = seatRepository.findByAuditorium_AuditoriumId(auditoriumId);

		return seats.stream().map(seat -> {
			boolean reserved = seat.getBookingSeats().stream()
					.anyMatch(bookingSeat -> bookingSeat.getScreen().getScreenId().equals(screenId));
			return new SeatResponse(seat.getSeatId(), seat.getSeatNumber(), reserved);
		}).collect(Collectors.toList());
	}

}
