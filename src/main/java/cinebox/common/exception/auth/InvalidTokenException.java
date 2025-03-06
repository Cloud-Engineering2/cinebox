package cinebox.common.exception.auth;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class InvalidTokenException extends BaseException {
	public static final BaseException EXCEPTION = new InvalidTokenException();
	
	private InvalidTokenException() {
		super(ExceptionMessage.INVALID_TOKEN);
	
	}
}
