package cinebox.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
	// user
	NOT_FOUND_USER("유효하지 않은 UserId 입니다.", HttpStatus.NOT_FOUND, "Not Found UserId"),
	ALREADY_EXIST_USER("사용 중인 아이디 입니다.", HttpStatus.BAD_REQUEST, "Already Exist UserId"),
	NOT_AUTHORIZED_USER("토큰 정보와 일치하는 사용자가 아닙니다.", HttpStatus.FORBIDDEN, "Not Authorized User"),
	ACCESS_DENIED_USER("해당 리소스에 접근할 권한이 없습니다.", HttpStatus.FORBIDDEN, "Access Denied"),
	
	// movie
	NOT_FOUND_MOVIE("유효하지 않은 MovieId 입니다.", HttpStatus.NOT_FOUND, "Not Found MovieId"),
	DUPLICATED_MOVIE("이미 존재하는 영화입니다.", HttpStatus.CONFLICT, "Conflict Title and ReleaseDate"),
  MOVIE_DELETE_FAILED("영화 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Delete Movie"),
	
	// booking- screen
	NOT_FOUND_SCREEN("유효하지 않은 ScreenId 입니다.", HttpStatus.NOT_FOUND, "Not Found ScreenId"),

	// booking- seat
	NOT_FOUND_SEAT("유효하지 않은 SeatId 입니다.", HttpStatus.NOT_FOUND, "Not Found SeatId"),
	SEAT_ALREADY_BOOKED("이미 예약된 좌석입니다.", HttpStatus.BAD_REQUEST, "Seat Already Booked"),
	
	// validation error
	VALIDATION_ERROR("빈 값을 허용하지 않습니다.", HttpStatus.BAD_REQUEST, "Validation Error"),
	
	// auditorium
	NOT_FOUND_AUDITORIUM("유효하지 않은 AuditoriumId 입니다.", HttpStatus.NOT_FOUND, "Not Found AuditoriumId"),
	
	// screen
	NOT_FOUND_SCREEN("유효하지 않은 ScreenId 입니다.", HttpStatus.NOT_FOUND, "Not Found ScreenId"),
	
	// booking
  SEAT_NOT_FOUND("좌석을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "Not Found Seat"),
	SCREEN_NOT_FOUND("상영 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND, "Not Found Screen for Booking");

	private final String message;
	private final HttpStatus status;
	private final String title;
}
