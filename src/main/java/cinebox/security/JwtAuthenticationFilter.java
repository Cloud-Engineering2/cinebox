package cinebox.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String token = resolveToken(request); // JWT 토큰 추출

		try {
			if (token != null && jwtTokenProvider.validateToken(token)) {
				// 토큰 유효
				Authentication auth = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth); // 생성된 Authentication 객체를
																			// SecurityContextHolder 에 설정
			}
		} catch (JWTVerificationException | IllegalArgumentException e) {
			// JWTVerificationException : JWT 유효 x | 서명 잘못됨
			// IllegalArgumentException : 잘못된 인자 전달
			logger.error(e.getMessage());

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 인증 실패 - 401 코드 반환
			response.getWriter().write("doFilterInternal() - Invalid JWT token");

			return;
		}
		chain.doFilter(request, response);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		logger.info("📌 Authorization 헤더: " + bearerToken); // ✅ 추가 로그
		return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
	}
}
