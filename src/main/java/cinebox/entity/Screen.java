package cinebox.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
	
	
//	@PrePersist
//    public void prePersist() {
//        if (this.startTime != null) {
//            // 원하는 포맷으로 변환
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            String formattedTime = this.startTime.format(formatter);
//            
//            // 포맷된 시간을 다시 LocalDateTime으로 변환해서 저장
//            this.startTime = LocalDateTime.parse(formattedTime, formatter);
//        }
//    }
}
