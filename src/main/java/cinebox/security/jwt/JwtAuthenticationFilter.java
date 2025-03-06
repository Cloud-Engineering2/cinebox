package cinebox.security.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import cinebox.common.utils.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		log.debug("## doFilterInternal 동작...");
		// 쿠키에서 액세스 토큰과 리프레시 토큰 추출
		Optional<Cookie> accessTokenCookie = CookieUtil.getCookie(request, "AT");
		Optional<Cookie> refreshTokenCookie = CookieUtil.getCookie(request, "RT");

		UsernamePasswordAuthenticationToken authentication = null;

		if (accessTokenCookie.isPresent() && jwtTokenProvider.validateToken(accessTokenCookie.get().getValue())) {
			// 유효한 액세스 토큰이 있는 경우
			authentication = jwtTokenProvider.createAuthenticationFromToken(accessTokenCookie.get().getValue());
		} else if (refreshTokenCookie.isPresent() && jwtTokenProvider.validateToken(refreshTokenCookie.get().getValue())) {
			// 액세스 토큰이 없거나 만료되었을 때, 리프레시 토큰으로 재발급 시도
			authentication = jwtTokenProvider.replaceAccessToken(response, refreshTokenCookie.get().getValue());
		}

		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}
}
