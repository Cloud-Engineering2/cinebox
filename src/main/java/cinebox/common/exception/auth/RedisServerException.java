package cinebox.common.exception.auth;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class RedisServerException extends BaseException {
	public static final BaseException EXCEPTION = new RedisServerException();

	private RedisServerException() {
		super(ExceptionMessage.REDIS_SERVER_ERROR);

	}
}
