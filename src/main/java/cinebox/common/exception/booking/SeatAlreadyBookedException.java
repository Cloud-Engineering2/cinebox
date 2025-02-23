package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class SeatAlreadyBookedException extends BaseException {
    public static final BaseException EXCEPTION = new SeatAlreadyBookedException();

    private SeatAlreadyBookedException() {
        super(ExceptionMessage.SEAT_ALREADY_BOOKED);
    }
}