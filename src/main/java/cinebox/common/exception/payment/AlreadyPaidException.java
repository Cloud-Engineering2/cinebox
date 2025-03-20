package cinebox.common.exception.payment;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class AlreadyPaidException extends BaseException {
	public static final BaseException EXCEPTION =  new AlreadyPaidException();
	
	private AlreadyPaidException() {
		super(ExceptionMessage.SEAT_ALREADY_PAYMENT);
		
	}

}
