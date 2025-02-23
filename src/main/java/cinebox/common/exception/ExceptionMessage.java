package cinebox.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
	// user
	NOT_FOUND_USER("유효하지 않은 UserId 입니다.", HttpStatus.NOT_FOUND, "Not Found UserId"),
	
	// movie
	NOT_FOUND_MOVIE("유효하지 않은 MovieId 입니다.", HttpStatus.NOT_FOUND, "Not Found MovieId"),
	DUPLICATED_MOVIE("이미 존재하는 영화입니다.", HttpStatus.CONFLICT, "Conflict Title and ReleaseDate"),
	
	// booking- screen
	NOT_FOUND_SCREEN("유효하지 않은 ScreenId 입니다.", HttpStatus.NOT_FOUND, "Not Found ScreenId"),

	// booking- seat
	NOT_FOUND_SEAT("유효하지 않은 SeatId 입니다.", HttpStatus.NOT_FOUND, "Not Found SeatId"),
	SEAT_ALREADY_BOOKED("이미 예약된 좌석입니다.", HttpStatus.BAD_REQUEST, "Seat Already Booked");


	
	private final String message;
	private final HttpStatus status;
	private final String title;
}
