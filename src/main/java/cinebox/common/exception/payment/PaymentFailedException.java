package cinebox.common.exception.payment;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class PaymentFailedException extends BaseException {

	private static final long serialVersionUID = 1L;

	public PaymentFailedException(ExceptionMessage message) {
		super(message);
		
	}

}
