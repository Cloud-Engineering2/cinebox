package cinebox.domain.screen.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cinebox.common.entity.BaseTimeEntity;
import cinebox.domain.auditorium.entity.Auditorium;
import cinebox.domain.booking.entity.BookingSeat;
import cinebox.domain.movie.entity.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "screen")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Screen extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "screen_id")
	private Long screenId;

	@ManyToOne
	@JoinColumn(name = "movie_id", nullable = false)
	private Movie movie;

	@ManyToOne
	@JoinColumn(name = "auditorium_id", nullable = false)
	private Auditorium auditorium;

	private LocalDateTime startTime;
	private LocalDateTime endTime;

	private BigDecimal price;

	@OneToMany(mappedBy = "screen")
	private List<BookingSeat> bookingSeats = new ArrayList<>();

	public void updateScreen(Movie movie, Auditorium auditorium, LocalDateTime startTime, BigDecimal price) {
		this.movie = movie;
		this.auditorium = auditorium;
		this.startTime = startTime;
		this.endTime = this.startTime.plusMinutes(movie.getRunTime() + 10);
		this.price = price != null ? price : this.price;
	}
}
