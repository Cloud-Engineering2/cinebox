package cinebox.common.exception.movie;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class S3ServerException extends BaseException {
	public static final BaseException EXCEPTION = new S3ServerException();
	
	private S3ServerException() {
		super(ExceptionMessage.S3_SERVER_ERROR);
	}
}
