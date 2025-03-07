package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class InsufficientAgeException extends BaseException {
	public static final BaseException EXCEPTION = new InsufficientAgeException();

	private InsufficientAgeException() {
		super(ExceptionMessage.INSUFFICIENT_AGE);
	}
}
