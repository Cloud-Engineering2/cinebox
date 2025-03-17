package cinebox.common.utils;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CookieUtil {
	private static String DOMAIN;
	
	@Value("${domain}")
	public void setDomain(String domain) {
		CookieUtil.DOMAIN = domain;
	}
	
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
		String accessCookie = String.format(
				"AT=;"
				+ "Path=/;"
				+ "Domain=%s;"
				+ "Max-Age=0;"
				+ "HttpOnly;"
				+ "Secure;"
				+ "SameSite=Lax",
				DOMAIN);
		response.addHeader("Set-Cookie", accessCookie);
		
		String refreshCookie = String.format("RT=;"
				+ "Path=/;"
				+ "Domain=%s;"
				+ "Max-Age=0;"
				+ "HttpOnly;"
				+ "Secure;"
				+ "SameSite=Lax",
				DOMAIN);
        response.addHeader("Set-Cookie", refreshCookie);
	}
}
