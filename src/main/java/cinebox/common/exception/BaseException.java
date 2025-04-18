package cinebox.common.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {
	private final HttpStatus status;
	private final String title;

	public BaseException(ExceptionMessage message) {
		super(message.getMessage());
		this.status = message.getStatus();
		this.title = message.getTitle();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}
}
