package cinebox.common.exception.movie;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundMovieException extends BaseException {
	public static final BaseException EXCEPTION = new NotFoundMovieException();
	
	private NotFoundMovieException() {
		super(ExceptionMessage.NOT_FOUND_MOVIE);
	}
}
