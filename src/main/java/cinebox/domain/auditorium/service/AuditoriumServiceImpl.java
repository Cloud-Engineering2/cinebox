package cinebox.domain.auditorium.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.auditorium.AlreadyExistAuditoriumException;
import cinebox.common.exception.auditorium.NotFoundAuditoriumException;
import cinebox.domain.auditorium.dto.AuditoriumRequest;
import cinebox.domain.auditorium.dto.AuditoriumResponse;
import cinebox.domain.auditorium.entity.Auditorium;
import cinebox.domain.auditorium.repository.AuditoriumRepository;
import cinebox.domain.seat.entity.Seat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditoriumServiceImpl implements AuditoriumService {
	
	private final AuditoriumRepository auditoriumRepository;

	@Override
	@Transactional(readOnly = true)
	public List<AuditoriumResponse> getAuditoriums() {
		log.info("서비스: 상영관 목록 조회 시작");
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
		log.info("서비스: 상영관 생성 시작: auditoriumName={}", request.auditoriumName());
		if (auditoriumRepository.existsByName(request.auditoriumName())) {
			log.error("서비스: 이미 존재하는 상영관: {}", request.auditoriumName());
			throw AlreadyExistAuditoriumException.EXCEPTION;
		}
		
		Auditorium auditorium = Auditorium.builder().name(request.auditoriumName()).build();
		
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
		
        log.info("서비스: 상영관 생성 완료, auditoriumId={}", auditorium.getAuditoriumId());
		return AuditoriumResponse.from(auditorium);
	}

	@Override
	@Transactional
	public AuditoriumResponse updateAuditorium(Long auditoriumId, AuditoriumRequest request) {
		log.info("서비스: 상영관 수정 시작: auditoriumId={}, 새이름={}", auditoriumId, request.auditoriumName());
		Auditorium auditorium = auditoriumRepository.findById(auditoriumId)
				.orElseThrow(() -> {
					log.error("서비스: 상영관을 찾을 수 없음: auditoriumId={}", auditoriumId);
					return NotFoundAuditoriumException.EXCEPTION;
				});

		auditorium.updateName(request.auditoriumName());

		Auditorium saved = auditoriumRepository.save(auditorium);
		log.info("서비스: 상영관 수정 완료: auditoriumId={}", auditoriumId);
		return AuditoriumResponse.from(saved);
	}
}
