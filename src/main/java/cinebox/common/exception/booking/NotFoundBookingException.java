package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundBookingException extends BaseException  {

	private static final long serialVersionUID = 1L;

	public static final BaseException EXCEPTION = new NotFoundBookingException();
	
	private NotFoundBookingException() {
		super(ExceptionMessage.NOT_FOUND_BOOKING);
	
	}

}
