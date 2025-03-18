package cinebox.common.utils;

import java.util.Optional;

import org.springframework.http.ResponseCookie;

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
	
	public static void clearAuthCookies(HttpServletResponse response) {
		log.info("Domain= {}", DOMAIN);
		
		ResponseCookie accessCookie = ResponseCookie.from("AT", "")
				.path("/")
				.domain("cine-box.store")
				.sameSite("None")
				.httpOnly(true)
				.secure(true)
				.maxAge(0)
				.build();
		response.addHeader("Set-Cookie", accessCookie.toString());
		log.info(accessCookie.toString());
		
		ResponseCookie refreshCookie = ResponseCookie.from("RT", "")
				.path("/")
				.domain("cine-box.store")
				.sameSite("None")
				.httpOnly(true)
				.secure(true)
				.maxAge(0)
				.build();
		response.addHeader("Set-Cookie", refreshCookie.toString());
		
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
