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
	
	// movie
	NOT_FOUND_MOVIE("유효하지 않은 MovieId 입니다.", HttpStatus.NOT_FOUND, "Not Found MovieId");
	
	private final String message;
	private final HttpStatus status;
	private final String title;
}
