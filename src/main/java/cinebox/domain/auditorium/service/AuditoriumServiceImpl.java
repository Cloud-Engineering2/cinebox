package cinebox.domain.auditorium.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.auditorium.AlreadyExistAuditoriumException;
import cinebox.domain.auditorium.dto.AuditoriumRequest;
import cinebox.domain.auditorium.dto.AuditoriumResponse;
import cinebox.domain.auditorium.entity.Auditorium;
import cinebox.domain.auditorium.repository.AuditoriumRepository;
import cinebox.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditoriumServiceImpl implements AuditoriumService {
	
	private final AuditoriumRepository auditoriumRepository;

	@Override
	@Transactional(readOnly = true)
	public List<AuditoriumResponse> getAuditoriums() {
		List<Auditorium> auditoriums = auditoriumRepository.findAll();
		
		return auditoriums.stream()
				.sorted(Comparator.comparing(Auditorium::getName))
				.map(AuditoriumResponse::from)
				.collect(Collectors.toList());
	}

	// 상영관 생성
	@Override
	@Transactional
	public AuditoriumResponse createAuditorium(AuditoriumRequest request) {
		if (auditoriumRepository.existsByName(request.name())) {
			throw AlreadyExistAuditoriumException.EXCEPTION;
		}
		
		Auditorium auditorium = Auditorium.builder().name(request.name()).build();
		
		char rowChar = request.row().charAt(0);
        List<Seat> seats = new ArrayList<>();
        for (char row = 'A'; row <= rowChar; row++) {
            for (int number = 1; number <= request.column(); number++) {
                seats.add(Seat.builder()
                        .auditorium(auditorium)
                        .seatNumber(row + String.valueOf(number))
                        .build());
            }
        }
        auditorium.getSeats().addAll(seats);
        auditorium = auditoriumRepository.save(auditorium);
		
		return AuditoriumResponse.from(auditorium);
	}
}
