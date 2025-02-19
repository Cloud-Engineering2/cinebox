package cinebox.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
	UNAUTH("미인증"),
	USER("유저"),
	ADMIN("관리자");
	
	private final String role;
}
