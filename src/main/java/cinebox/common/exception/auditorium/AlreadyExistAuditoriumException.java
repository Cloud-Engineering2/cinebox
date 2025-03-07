package cinebox.common.exception.auditorium;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class AlreadyExistAuditoriumException extends BaseException {
public static final BaseException EXCEPTION = new AlreadyExistAuditoriumException();
	
	private AlreadyExistAuditoriumException() {
		super(ExceptionMessage.ALREADY_EXIST_AUDITORIUM);
	}
}
