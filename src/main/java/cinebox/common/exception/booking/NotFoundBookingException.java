package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundBookingException extends BaseException  {
	public static final BaseException EXCEPTION = new NotFoundBookingException();
	
	private NotFoundBookingException() {
		super(ExceptionMessage.NOT_FOUND_BOOKING);
	
	}

}
