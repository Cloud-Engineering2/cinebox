package cinebox.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
        	errorMessageBuilder
	        	.append(error.getField())
	        	.append(" : ")
	        	.append(error.getDefaultMessage())
	        	.append(" ");
        });
        
        if (errorMessageBuilder.length() > 0) {
        	errorMessageBuilder.setLength(errorMessageBuilder.length() - 1);
        }
        
        String detailedMessage = errorMessageBuilder.toString();
        
        ErrorResponse errorResponse = ErrorResponse.from(
                ExceptionMessage.VALIDATION_ERROR.getStatus().value(),
                ExceptionMessage.VALIDATION_ERROR.getTitle(),
                detailedMessage
        );
        return new ResponseEntity<>(errorResponse, ExceptionMessage.VALIDATION_ERROR.getStatus());
    }
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
	    logger.warn("AccessDeniedException: {}", e.getMessage());
	    
	    ErrorResponse errorResponse = ErrorResponse.from(
	            HttpStatus.FORBIDDEN.value(),
	            ExceptionMessage.ACCESS_DENIED_USER.getTitle(),
	            ExceptionMessage.ACCESS_DENIED_USER.getMessage()
	    );
	    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}
}
