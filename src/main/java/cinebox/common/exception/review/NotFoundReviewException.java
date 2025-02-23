package cinebox.common.exception.review;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class NotFoundReviewException extends BaseException {
	public static final BaseException EXCEPTION = new NotFoundReviewException();
	
	private NotFoundReviewException() {
		super(ExceptionMessage.NOT_FOUND_REVIEW);
	}
}
