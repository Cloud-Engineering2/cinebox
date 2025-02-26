package cinebox.service;

import cinebox.dto.ScreenRequest;
import cinebox.entity.Auditorium;
import cinebox.entity.Movie;
import cinebox.entity.Screen;
import cinebox.repository.AuditoriumRepository;
import cinebox.repository.MovieRepository;
import cinebox.repository.ScreenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScreenService {
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;
    private final AuditoriumRepository auditoriumRepository;

    // 상영 정보 추가
    @Transactional
    public Screen createScreen(ScreenRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("영화를 찾을 수 없습니다."));
        Auditorium auditorium = auditoriumRepository.findById(request.getAuditoriumId())
                .orElseThrow(() -> new EntityNotFoundException("상영관을 찾을 수 없습니다."));

        Screen screen = Screen.builder()
                .movie(movie)
                .auditorium(auditorium)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .price(request.getPrice())
                .build();

        return screenRepository.save(screen);
    }

    // 상영 정보 수정
    @Transactional
    public Screen updateScreen(Long screenId, ScreenRequest request) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상영 정보를 찾을 수 없습니다."));

        screen = Screen.builder()
                .screenId(screenId)
                .movie(screen.getMovie())
                .auditorium(screen.getAuditorium())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .price(request.getPrice())
                .build();

        return screenRepository.save(screen);
    }

    // 상영 정보 삭제
    @Transactional
    public void deleteScreen(Long screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상영 정보를 찾을 수 없습니다."));
        screenRepository.delete(screen);
    }
}
