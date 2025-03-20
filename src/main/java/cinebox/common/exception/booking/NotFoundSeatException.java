package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;


public class NotFoundSeatException extends BaseException {

	public static final BaseException EXCEPTION = new NotFoundSeatException();

    private NotFoundSeatException() {
        super(ExceptionMessage.NOT_FOUND_SEAT);
    }
}