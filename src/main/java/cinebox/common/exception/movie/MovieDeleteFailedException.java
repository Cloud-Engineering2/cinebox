package cinebox.common.exception.movie;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class MovieDeleteFailedException extends BaseException {
	public static final BaseException EXCEPTION = new MovieDeleteFailedException();

	private MovieDeleteFailedException() {
		super(ExceptionMessage.MOVIE_DELETE_FAILED);
	}
}
