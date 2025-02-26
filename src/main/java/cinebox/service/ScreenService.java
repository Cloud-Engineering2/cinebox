package cinebox.service;

import cinebox.dto.ScreenRequest;
import cinebox.dto.ScreenResponseDto;
import cinebox.entity.Auditorium;
import cinebox.entity.Movie;
import cinebox.entity.Screen;
import cinebox.exception.NotFoundException;
import cinebox.repository.AuditoriumRepository;
import cinebox.repository.MovieRepository;
import cinebox.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScreenService {
    private final ScreenRepository screenRepository;
    private final MovieRepository movieRepository;
    private final AuditoriumRepository auditoriumRepository;

    // 상영 정보 추가
    @Transactional
    public ScreenResponseDto createScreen(ScreenRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new NotFoundException("영화를 찾을 수 없습니다. movieId: " + request.getMovieId()));

        Auditorium auditorium = auditoriumRepository.findById(request.getAuditoriumId())
                .orElseThrow(() -> new NotFoundException("상영관을 찾을 수 없습니다. auditoriumId: " + request.getAuditoriumId()));

        LocalDateTime endTime = request.getStartTime().plusMinutes(movie.getRunTime()); // 🎯 endTime 자동 계산

        Screen screen = Screen.builder()
                .movie(movie)
                .auditorium(auditorium)
                .startTime(request.getStartTime())
                .endTime(endTime)
                .price(request.getPrice())
                .build();

        screenRepository.save(screen);
        return new ScreenResponseDto(screen);
    }

    // 상영 정보 수정
    @Transactional
    public ScreenResponseDto updateScreen(Long screenId, ScreenRequest request) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new NotFoundException("해당 상영 정보를 찾을 수 없습니다. screenId: " + screenId));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new NotFoundException("영화를 찾을 수 없습니다. movieId: " + request.getMovieId()));

        Auditorium auditorium = auditoriumRepository.findById(request.getAuditoriumId())
                .orElseThrow(() -> new NotFoundException("상영관을 찾을 수 없습니다. auditoriumId: " + request.getAuditoriumId()));

        LocalDateTime endTime = request.getStartTime().plusMinutes(movie.getRunTime()); // 🎯 endTime 자동 계산

        screen = Screen.builder()
                .screenId(screenId)
                .movie(movie)
                .auditorium(auditorium)
                .startTime(request.getStartTime())
                .endTime(endTime)
                .price(request.getPrice())
                .build();

        screenRepository.save(screen);
        return new ScreenResponseDto(screen);
    }

    // 상영 정보 삭제
    @Transactional
    public void deleteScreen(Long screenId) {
        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new NotFoundException("해당 상영 정보를 찾을 수 없습니다. screenId: " + screenId));
        screenRepository.delete(screen);
    }
}
