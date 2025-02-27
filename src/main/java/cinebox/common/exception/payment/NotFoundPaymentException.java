package cinebox.common.exception.payment;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundPaymentException  extends BaseException{


	private static final long serialVersionUID = 1L;

	public static final BaseException EXCEPTION = new NotFoundPaymentException();
	private  NotFoundPaymentException() {
		super(ExceptionMessage.NOT_FOUND_PAYMENT);
		
	}

}
