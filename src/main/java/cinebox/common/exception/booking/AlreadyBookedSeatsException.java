package cinebox.common.exception.booking;

import cinebox.common.exception.BaseException;
import cinebox.common.exception.ExceptionMessage;

public class AlreadyBookedSeatsException extends BaseException {

	 public static final BaseException EXCEPTION = new AlreadyBookedSeatsException();
	 
	 
    private AlreadyBookedSeatsException() {
    	 super(ExceptionMessage.SEAT_ALREADY_BOOKED);
    }
    
    
    // 사용자 정의 메시지를 받는 생성자
    public AlreadyBookedSeatsException(String message) {
        super(message, ExceptionMessage.SEAT_ALREADY_BOOKED.getStatus());
    }
    
   
    
}