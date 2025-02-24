package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundScreenException extends BaseException {
    public static final BaseException EXCEPTION = new NotFoundScreenException();

    private NotFoundScreenException() {
        super(ExceptionMessage.NOT_FOUND_SCREEN);
    }
}