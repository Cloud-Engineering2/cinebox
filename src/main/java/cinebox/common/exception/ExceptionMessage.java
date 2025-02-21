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

	
	// movie
	NOT_FOUND_MOVIE("유효하지 않은 MovieId 입니다.", HttpStatus.NOT_FOUND, "Not Found MovieId"),
	DUPLICATED_MOVIE("이미 존재하는 영화입니다.", HttpStatus.CONFLICT, "Conflict Title and ReleaseDate");
	
	private final String message;
	private final HttpStatus status;
	private final String title;
}
