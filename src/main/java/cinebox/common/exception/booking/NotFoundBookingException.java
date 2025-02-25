package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundBookingException extends BaseException  {

	private static final long serialVersionUID = 1L;

	
	public NotFoundBookingException(ExceptionMessage message) {
		super(ExceptionMessage.NOT_FOUND_BOOKING);
	
	}

}
