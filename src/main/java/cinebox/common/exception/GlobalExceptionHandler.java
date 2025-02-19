package cinebox.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handelBaseException(BaseException e, WebRequest request) {
		logger.warn("{} : {}", e.getClass(), e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.from(
				e.getStatus().value(),
				e.getTitle(),
				e.getMessage()
		);
		return new ResponseEntity<>(errorResponse, e.getStatus());
	}
}
