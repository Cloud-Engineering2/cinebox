package cinebox.common.exception.movie;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class DuplicatedMovieException extends BaseException {
	public static final BaseException EXCEPTION = new DuplicatedMovieException();

	private DuplicatedMovieException() {
		super(ExceptionMessage.DUPLICATED_MOVIE);
	}
}
