package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class AlreadyBookedSeatsException extends BaseException {
	public static final BaseException EXCEPTION = new AlreadyBookedSeatsException();

	private AlreadyBookedSeatsException() {
		super(ExceptionMessage.SEAT_ALREADY_BOOKED);
	}

}