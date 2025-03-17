package cinebox.common.utils;

import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtil {
	public static String DOMAIN;
	
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
	
	public static void clearAuthCookies(HttpServletRequest request, HttpServletResponse response) {
		log.info("Domain= {}", DOMAIN);
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("AT") || cookie.getName().equals("RT")) {
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
		
//		String accessCookie = String.format(
//				"AT=;"
//				+ "Path=/;"
//				+ "Domain=%s;"
//				+ "Max-Age=10000;"
//				+ "HttpOnly;"
//				+ "Secure;"
//				+ "SameSite=Lax",
//				DOMAIN);
//		response.addHeader("Set-Cookie", accessCookie);
//		
//		String refreshCookie = String.format("RT=;"
//				+ "Path=/;"
//				+ "Domain=%s;"
//				+ "Max-Age=10000;"
//				+ "HttpOnly;"
//				+ "Secure;"
//				+ "SameSite=Lax",
//				DOMAIN);
//        response.addHeader("Set-Cookie", refreshCookie);
	}
}
