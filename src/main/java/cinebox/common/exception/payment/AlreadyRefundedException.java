package cinebox.common.exception.payment;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class AlreadyRefundedException extends BaseException {
	public static final BaseException EXCEPTION = new AlreadyRefundedException();
	
	private  AlreadyRefundedException() {
		super(ExceptionMessage.ALREADY_REFUNDED);
	}
}
