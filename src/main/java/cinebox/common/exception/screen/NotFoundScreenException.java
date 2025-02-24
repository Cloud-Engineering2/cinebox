package cinebox.common.exception.screen;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;
import cinebox.common.exception.movie.NotFoundMovieException;

public class NotFoundScreenException extends BaseException {
	public static final BaseException EXCEPTION = new NotFoundScreenException();
	
	private NotFoundScreenException() {
		super(ExceptionMessage.NOT_FOUND_SCREEN);
	}
}
