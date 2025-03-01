package cinebox.common.exception.payment;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class InvalidPaymentStatusException extends BaseException {
	public static final BaseException EXCEPTION = new InvalidPaymentStatusException();

	private  InvalidPaymentStatusException() {
		super(ExceptionMessage.INVALID_PAYMENT_STATUS);
	}
}
