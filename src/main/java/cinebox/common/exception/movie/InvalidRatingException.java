package cinebox.common.exception.movie;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class InvalidRatingException extends BaseException {
	public static final BaseException EXCEPTION = new InvalidRatingException();

	private InvalidRatingException() {
		super(ExceptionMessage.INVALID_RATING);
	}
}
