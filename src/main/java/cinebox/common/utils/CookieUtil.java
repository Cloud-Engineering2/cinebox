package cinebox.common.utils;

import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return Optional.of(cookie);
				}
			}
		}
		return Optional.empty();
	}
	
	public static void clearAuthCookies(HttpServletResponse response) {
		Cookie accessTokenCookie = new Cookie("AT", "");
		accessTokenCookie.setPath("/");
		accessTokenCookie.setMaxAge(0);
		accessTokenCookie.setHttpOnly(true);

		Cookie refreshTokenCookie = new Cookie("RT", "");
		refreshTokenCookie.setPath("/");
		refreshTokenCookie.setMaxAge(0);
		refreshTokenCookie.setHttpOnly(true);

		response.addCookie(accessTokenCookie);
		response.addCookie(refreshTokenCookie);
	}
}
