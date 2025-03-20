package cinebox.common.exception.user;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class DuplicatedIdentifierException extends BaseException {
	public static final BaseException EXCEPTION = new DuplicatedIdentifierException();
	
	private DuplicatedIdentifierException() {
		super(ExceptionMessage.ALREADY_EXIST_ID);
	}
}
