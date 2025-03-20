package cinebox.common.exception.auth;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class TokenParsingException extends BaseException {
	public static final BaseException EXCEPTION = new TokenParsingException();

	private TokenParsingException() {
		super(ExceptionMessage.TOKEN_PARSING_ERROR);

	}
}
