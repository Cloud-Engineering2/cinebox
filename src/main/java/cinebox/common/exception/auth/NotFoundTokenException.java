package cinebox.common.exception.auth;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundTokenException extends BaseException {
	public static final BaseException EXCEPTION = new NotFoundTokenException();
	
	private NotFoundTokenException() {
		super(ExceptionMessage.NOT_FOUND_TOKEN);
	
	}
}
