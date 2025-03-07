package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class InsufficientBookingStatusException extends BaseException {
	public static final BaseException EXCEPTION = new InsufficientBookingStatusException();

	private InsufficientBookingStatusException() {
		super(ExceptionMessage.INSUFFICIENT_BOOKING_STATUS);
	}
}