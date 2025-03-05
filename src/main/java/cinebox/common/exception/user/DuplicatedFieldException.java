package cinebox.common.exception.user;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class DuplicatedFieldException extends BaseException {
	public static final BaseException EXCEPTION = new DuplicatedFieldException();

	private DuplicatedFieldException() {
		super(ExceptionMessage.ALREADY_EXIST_FIELD);
	}
}
