package cinebox.security.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import cinebox.common.exception.ErrorResponse;
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
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		log.debug("## doFilterInternal 동작...");
		// 쿠키에서 액세스 토큰과 리프레시 토큰 추출
		Optional<Cookie> accessTokenCookie = CookieUtil.getCookie(request, "AT");
		Optional<Cookie> refreshTokenCookie = CookieUtil.getCookie(request, "RT");

		UsernamePasswordAuthenticationToken authentication = null;

		if (accessTokenCookie.isPresent()) {
			String accessToken = accessTokenCookie.get().getValue();
			
			// 블랙리스트 토큰 검증
			if (jwtTokenProvider.isTokenBlacklisted(accessToken)) {
				log.warn("블랙리스트에 등록된 토큰이 접속을 시도합니다. 요청 거부");
				sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid Token", "유효하지 않은 토큰입니다. 다시 로그인 해 주세요.");
				return;
			}
			
			// 유효한 액세스 토큰이 있는 경우
			if (jwtTokenProvider.validateToken(accessToken)) {
				authentication = jwtTokenProvider.createAuthenticationFromToken(accessTokenCookie.get().getValue());
			}
		} else if (refreshTokenCookie.isPresent() && jwtTokenProvider.validateToken(refreshTokenCookie.get().getValue())) {
			// 액세스 토큰이 없거나 만료되었을 때, 리프레시 토큰으로 재발급 시도
			authentication = jwtTokenProvider.replaceAccessToken(response, refreshTokenCookie.get().getValue());
		}

		if (authentication != null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}
	
	private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String title, String message) throws IOException {
		ErrorResponse errorResponse = ErrorResponse.from(status.value(), title, message);
		response.setStatus(status.value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		response.getWriter().flush();
	}
}
