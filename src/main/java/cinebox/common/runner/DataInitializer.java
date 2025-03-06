package cinebox.common.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cinebox.domain.auditorium.entity.Auditorium;
import cinebox.domain.auditorium.repository.AuditoriumRepository;
import cinebox.domain.seat.entity.Seat;
import cinebox.domain.seat.repository.SeatRepository;

@Component
public class DataInitializer implements CommandLineRunner {
	private final AuditoriumRepository auditoriumRepository;
	private final SeatRepository seatRepository;
	
	public DataInitializer(AuditoriumRepository auditoriumRepository, SeatRepository seatRepository) {
		this.auditoriumRepository = auditoriumRepository;
		this.seatRepository = seatRepository;
	}
	
	@Override
	public void run(String... args) throws Exception {
		if (auditoriumRepository.count() == 0) {
			List<Auditorium> auditoriums = IntStream.rangeClosed(1, 6)
                    .mapToObj(i -> Auditorium.builder().name("상영관 " + i).capacity(190).build())
                    .toList();

            auditoriumRepository.saveAll(auditoriums);

            for (Auditorium auditorium : auditoriums) {
                List<Seat> seats = new ArrayList<>();
                for (char row = 'A'; row <= 'J'; row++) {
                    for (int number = 1; number <= 10; number++) {
                        seats.add(Seat.builder()
                                .auditorium(auditorium)
                                .seatNumber(row + String.valueOf(number))
                                .build());
                    }
                }
                seatRepository.saveAll(seats);
            }
        }
	}
}
