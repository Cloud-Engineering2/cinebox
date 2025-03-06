package cinebox.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
	// auth
	NOT_FOUND_TOKEN("토큰을 조회할 수 없습니다. 다시 로그인 해 주세요.", HttpStatus.UNAUTHORIZED, "Not Found Token"),
	INVALID_TOKEN("유효하지 않은 토큰입니다. 다시 로그인 해 주세요.", HttpStatus.UNAUTHORIZED, "Invaled Token"),
	REDIS_SERVER_ERROR("Redis 서버 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR, "Redis Server Error"),
	
	// user
	NOT_FOUND_USER("유효하지 않은 UserId 입니다.", HttpStatus.NOT_FOUND, "Not Found UserId"),
	ALREADY_EXIST_ID("사용 중인 아이디 입니다.", HttpStatus.CONFLICT, "Already Exist ID"),
	ALREADY_EXIST_EMAIL("이미 등록된 이메일입니다.", HttpStatus.CONFLICT, "Already Exist Email"),
	ALREADY_EXIST_PHONE("이미 등록된 전화번호입니다.", HttpStatus.CONFLICT, "Already Exist Phone"),
	ALREADY_EXIST_FIELD("중복된 필드가 존재합니다.", HttpStatus.CONFLICT, "Duplicated Field Exists"),
	NOT_AUTHORIZED_USER("토큰 정보와 일치하는 사용자가 아닙니다.", HttpStatus.FORBIDDEN, "Not Authorized User"),
	ACCESS_DENIED_USER("해당 리소스에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN, "Access Denied"),

	// movie
	NOT_FOUND_MOVIE("유효하지 않은 MovieId 입니다.", HttpStatus.NOT_FOUND, "Not Found MovieId"),
	DUPLICATED_MOVIE("이미 존재하는 영화입니다.", HttpStatus.CONFLICT, "Conflict Title and ReleaseDate"),
	MOVIE_DELETE_FAILED("영화 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Delete Movie"),
	
	// s3
	S3_SERVER_ERROR("S3 서버 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR, "S3 Server Error"),
  
	// screen
	NOT_FOUND_SCREEN("유효하지 않은 ScreenId 입니다.", HttpStatus.NOT_FOUND, "Not Found ScreenId"),
	SCREEN_TIME_CONFLICT("해당 상영관 및 시간에 상영할 수 없습니다.", HttpStatus.BAD_REQUEST, "Screen Time Conflict"),
	
	// booking- seat
	NOT_FOUND_SEAT("유효하지 않은 SeatId 입니다.", HttpStatus.NOT_FOUND, "Not Found SeatId"),
	SEAT_ALREADY_BOOKED("이미 예약된 좌석입니다.", HttpStatus.BAD_REQUEST, "Seat Already Booked"),

	// review
	NOT_FOUND_REVIEW("유효하지 않은 ReviewId 입니다.", HttpStatus.NOT_FOUND, "Not Found ReviewId"),
  
	// validation error
	VALIDATION_ERROR("빈 값을 허용하지 않습니다.", HttpStatus.BAD_REQUEST, "Validation Error"),

	// auditorium
	NOT_FOUND_AUDITORIUM("유효하지 않은 AuditoriumId 입니다.", HttpStatus.NOT_FOUND, "Not Found AuditoriumId"),

	// booking
	SEAT_NOT_FOUND("좌석을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "Not Found Seat"),
	SCREEN_NOT_FOUND("상영 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "Not Found Screen for Booking"),
	NOT_FOUND_BOOKING("예매된 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "Not Found booking information"),
	ALREADY_REFUNDED("이미 환불된 예매입니다.", HttpStatus.BAD_REQUEST, "Already Refunded"),
	
	//payment
	SEAT_ALREADY_PAYMENT("이미 결제가 완료되었습니다", HttpStatus.BAD_REQUEST,"Payment has already been completed"),
	NOT_FOUND_PAYMENT("결제정보를 찾지못했습니다.", HttpStatus.NOT_FOUND, "Not Found Payment information"),
	NOT_PAID_BOOKING("결제가 완료되지 않은 예매입니다.", HttpStatus.BAD_REQUEST, "Not Paid Booking"),
	INVALID_PAYMENT_STATUS("결제를 진행할 수 없습니다. 결제 상태를 확인해주세요.", HttpStatus.BAD_REQUEST, "Invalid Payment Status");

	private final String message;
	private final HttpStatus status;
	private final String title;
}
