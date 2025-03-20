package cinebox.common.exception.screen;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class ScreenTimeConflictException extends BaseException {
public static final BaseException EXCEPTION = new ScreenTimeConflictException();
	
	private ScreenTimeConflictException() {
		super(ExceptionMessage.SCREEN_TIME_CONFLICT);
	}
}
