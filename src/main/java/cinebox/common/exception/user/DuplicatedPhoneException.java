package cinebox.common.exception.user;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class DuplicatedPhoneException extends BaseException {
	public static final BaseException EXCEPTION = new DuplicatedPhoneException();

	private DuplicatedPhoneException() {
		super(ExceptionMessage.ALREADY_EXIST_PHONE);
	}
}
