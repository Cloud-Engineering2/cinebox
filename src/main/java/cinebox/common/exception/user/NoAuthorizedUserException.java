package cinebox.common.exception.user;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NoAuthorizedUserException extends BaseException {
	public static final BaseException EXCEPTION = new NoAuthorizedUserException();
	
	private NoAuthorizedUserException() {
		super(ExceptionMessage.ACCESS_DENIED_USER);
	}
}
