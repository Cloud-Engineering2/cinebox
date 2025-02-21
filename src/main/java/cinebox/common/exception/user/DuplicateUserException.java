package cinebox.common.exception.user;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class DuplicateUserException extends BaseException {
	public static final BaseException EXCEPTION = new DuplicateUserException();
	
	private DuplicateUserException() {
		super(ExceptionMessage.ALREADY_EXIST_USER);
	}
}
