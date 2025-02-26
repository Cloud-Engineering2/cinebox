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

	// 추가 
    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        this.title = null; // Or you can provide a default title if needed
    }

    
	public HttpStatus getStatus() {
		return status;
	}
	
	public String getTitle() {
		return title;
	}
}
