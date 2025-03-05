package cinebox.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.dto.response.AuditoriumResponse;
import cinebox.entity.Auditorium;
import cinebox.repository.AuditoriumRepository;
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
}
