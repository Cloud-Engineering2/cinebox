package cinebox.common.exception.auth;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;
import cinebox.common.exception.booking.NotFoundBookingException;

public class NotFoundTokenException extends BaseException {
	public static final BaseException EXCEPTION = new NotFoundTokenException();
	
	private NotFoundTokenException() {
		super(ExceptionMessage.NOT_FOUND_TOKEN);
	
	}
}
