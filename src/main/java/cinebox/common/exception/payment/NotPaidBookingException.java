package cinebox.common.exception.payment;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotPaidBookingException extends BaseException {
	public static final BaseException EXCEPTION = new NotPaidBookingException();
	
	private NotPaidBookingException() {
		super(ExceptionMessage.NOT_PAID_BOOKING);
	}
}
