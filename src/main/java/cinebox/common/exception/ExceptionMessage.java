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
