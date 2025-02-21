package cinebox.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	
	/*
	 * @Valid 어노테이션 예외 처리
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException e, WebRequest request) {
        logger.warn("Validation error: {}", e.getMessage());
        
        StringBuilder errorMessageBuilder = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error -> {
        	errorMessageBuilder.append(error.getField()).append(", ");
        });
        
        if (errorMessageBuilder.length() > 0) {
        	errorMessageBuilder.setLength(errorMessageBuilder.length() - 2);
        }
        
        String detailedMessage = errorMessageBuilder.toString()
        		+ ": " 
        		+ ExceptionMessage.VALIDATION_ERROR.getMessage();
        
        ErrorResponse errorResponse = ErrorResponse.from(
                ExceptionMessage.VALIDATION_ERROR.getStatus().value(),
                ExceptionMessage.VALIDATION_ERROR.getTitle(),
                detailedMessage
        );
        return new ResponseEntity<>(errorResponse, ExceptionMessage.VALIDATION_ERROR.getStatus());
    }
}
