package cinebox.common.exception.user;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class DuplicatedEmailException extends BaseException{
	public static final BaseException EXCEPTION = new DuplicatedEmailException();

	private DuplicatedEmailException() {
		super(ExceptionMessage.ALREADY_EXIST_EMAIL);
	}
}
