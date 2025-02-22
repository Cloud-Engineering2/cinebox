package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import cinebox.common.exception.auditorium.NotFoundAuditoriumException;
import cinebox.common.exception.screen.NotFoundScreenException;
import cinebox.dto.response.SeatResponse;
import cinebox.entity.Auditorium;
import cinebox.entity.Screen;
import cinebox.entity.Seat;
import cinebox.repository.AuditoriumRepository;
import cinebox.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
	private final ScreenRepository screenRepository;
	private final AuditoriumRepository auditoriumRepository;

	// 좌석 조회
	@Override
	public List<SeatResponse> getSeats(Long screenId) {
		Screen screen = screenRepository.findById(screenId)
				.orElseThrow(() -> NotFoundScreenException.EXCEPTION);
		
		Long auditoriumId = screen.getAuditorium().getAuditoriumId();
		Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
				.orElseThrow(() -> NotFoundAuditoriumException.EXCEPTION);
		List<Seat> seats = auditorium.getSeats();

		return seats.stream().map(seat -> {
			boolean reserved = seat.getBookingSeats().stream()
					.anyMatch(bookingSeat -> bookingSeat.getScreen().getScreenId().equals(screenId));
			return new SeatResponse(seat.getSeatId(), seat.getSeatNumber(), reserved);
		}).collect(Collectors.toList());
	}

}
