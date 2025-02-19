package cinebox.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cinebox.common.enums.MovieStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movie")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movie extends BaseTimeEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "movie_id")
    private Long movieId;

    @Column(nullable = false)
    private String title;

    // 줄거리 (description)
    private String plot;
    
    private String director;
    private String actor;
    private String genre;

    private String posterImageUrl;

    private LocalDate releaseDate;

    private Integer runTime;
    
    private String rating_grade;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MovieStatus status;
    
    @OneToMany(mappedBy = "movie")
    private List<Screen> screens = new ArrayList<>();

    // 한 영화에 여러 리뷰(Review)가 있을 수 있음 (1:N)
    @OneToMany(mappedBy = "movie")
    private List<Review> reviews = new ArrayList<>();
}
