package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class AgeVerificationException extends BaseException {
	public static final BaseException EXCEPTION = new AgeVerificationException();

	private AgeVerificationException() {
		super(ExceptionMessage.AGE_VERIFICATION_FAILED);
	}
}
