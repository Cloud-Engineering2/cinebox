package cinebox.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequest {
	private Long reviewId;
	private Long movieId;
	private Long userId;
	private int rating;
	private String content;
}
