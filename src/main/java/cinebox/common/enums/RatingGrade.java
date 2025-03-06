package cinebox.common.enums;

import cinebox.common.exception.movie.InvalidRatingException;
import lombok.Getter;

@Getter
public enum RatingGrade {
	ALL("전체관람가"),
	AGE_12("12세관람가"),
	AGE_15("15세관람가"),
	AGE_18("청소년관람불가"),
	NOT_SET("등급미설정");

	private final String label;

	RatingGrade(String label) {
		this.label = label;
	}

	public static RatingGrade fromLabel(String label) {
		if (label == null) {
			return NOT_SET;
		}
		for (RatingGrade rating : values()) {
			if(rating.getLabel().equals(label)) {
				return rating;
			}
		}
		
		throw InvalidRatingException.EXCEPTION;
	}
}
