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
	DUPLICATED_MOVIE("이미 존재하는 영화입니다.", HttpStatus.CONFLICT, "Conflict Title and ReleaseDate"),
	MOVIE_DELETE_FAILED("영화 삭제에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR, "Failed to Delete Movie"),
	
	// review
	NOT_FOUND_REVIEW("유효하지 않은 ReviewId 입니다.", HttpStatus.NOT_FOUND, "Not Found ReviewId"),
	
	// validation error
	VALIDATION_ERROR("빈 값을 허용하지 않습니다.", HttpStatus.BAD_REQUEST, "Validation Error");
	
	private final String message;
	private final HttpStatus status;
	private final String title;
}
