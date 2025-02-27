package cinebox.common.exception.payment;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class AlreadyPaidException extends BaseException {

	private static final long serialVersionUID = 1L;

	public AlreadyPaidException(ExceptionMessage message) {
		super(ExceptionMessage.SEAT_ALREADY_PAYMENT);
		
	}

}
