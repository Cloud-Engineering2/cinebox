package cinebox.common.exception.user;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundUserException extends BaseException {
	public static final BaseException EXCEPTION = new NotFoundUserException();
	
	private NotFoundUserException() {
		super(ExceptionMessage.NOT_FOUND_USER);
	}
}
