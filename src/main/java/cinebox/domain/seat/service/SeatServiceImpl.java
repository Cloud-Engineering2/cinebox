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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
	private final ScreenRepository screenRepository;
	private final SeatRepository seatRepository;

	// 좌석 조회
	@Override
	public List<SeatResponse> getSeats(Long screenId) {
		log.info("좌석 조회 서비스 시작: screenId={}", screenId);
		Screen screen = screenRepository.findById(screenId)
				.orElseThrow(() -> {
					log.error("좌석 조회 실패: screenId={} 조회 결과 없음", screenId);
					return NotFoundScreenException.EXCEPTION;
				});
		
		Long auditoriumId = screen.getAuditorium().getAuditoriumId();
		List<Seat> seats = seatRepository.findByAuditorium_AuditoriumId(auditoriumId);

		List<SeatResponse> responses = seats.stream().map(seat -> {
			boolean reserved = seat.getBookingSeats().stream()
					.anyMatch(bookingSeat -> bookingSeat.getScreen().getScreenId().equals(screenId));
			return new SeatResponse(seat.getSeatId(), seat.getSeatNumber(), reserved);
		}).collect(Collectors.toList());
		
		log.info("좌석 조회 서비스 완료: screenId={}, 좌석 수={}", screenId, responses.size());
		return responses;
	}

}
