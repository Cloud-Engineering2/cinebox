package cinebox.common.exception.auditorium;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;
import cinebox.common.exception.movie.NotFoundMovieException;

public class NotFoundAuditoriumException extends BaseException {
	public static final BaseException EXCEPTION = new NotFoundAuditoriumException();
	
	private NotFoundAuditoriumException() {
		super(ExceptionMessage.NOT_FOUND_AUDITORIUM);
	}
}
