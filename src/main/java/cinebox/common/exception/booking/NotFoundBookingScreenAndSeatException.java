package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundBookingScreenAndSeatException extends BaseException {

    private static final long serialVersionUID = 1L;

    // EXCEPTION은 이제 하나로만 정의하고, 두 가지 메시지를 다룰 수 있도록 처리
    public static final BaseException EXCEPTION_SEAT_NOT_FOUND = new NotFoundBookingScreenAndSeatException(ExceptionMessage.SEAT_NOT_FOUND);
    public static final BaseException EXCEPTION_SCREEN_NOT_FOUND = new NotFoundBookingScreenAndSeatException(ExceptionMessage.SCREEN_NOT_FOUND);

    // 생성자에서 메시지를 받도록 처리
    private NotFoundBookingScreenAndSeatException(ExceptionMessage message) {
        super(message);
    }
}