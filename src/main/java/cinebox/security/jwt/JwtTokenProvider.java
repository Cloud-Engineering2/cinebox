package cinebox.security.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import cinebox.common.enums.PlatformType;
import cinebox.common.exception.auth.InvalidTokenException;
import cinebox.common.exception.auth.NotFoundTokenException;
import cinebox.common.exception.auth.RedisServerException;
import cinebox.domain.auth.entity.TokenRedis;
import cinebox.domain.auth.repository.TokenRedisRepository;
import cinebox.security.service.PrincipalDetails;
import cinebox.security.service.PrincipalDetailsService;
import io.lettuce.core.RedisException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {
	private final PrincipalDetailsService principalDetailsService;
	private final TokenRedisRepository tokenRedisRepository;
	private final RedisKeyValueTemplate redisKeyValueTemplate;
	private final RedisTemplate<String, String> redisStringTemplate;

	@Value("${security.jwt.secretkey}")
	private String secretKey;

	@Value("${security.jwt.accessTokenValidityInMilliseconds}")
	public long accessTokenValidityInMilliseconds;

	@Value("${security.jwt.refreshTokenValidityInMilliseconds}")
	public long refreshTokenValidityInMilliseconds;

	@Value("${domain}")
	public String cookieDomain;

	public String createAccessToken(Long userId, String role, PlatformType platformType, String identifier) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);
		return JWT.create()
				.withClaim("user_id", userId)
				.withClaim("role", role)
				.withClaim("platform", platformType.name())
				.withClaim("identifier", identifier)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.sign(Algorithm.HMAC256(secretKey));
	}

	public String createRefreshToken(Long userId, String role, PlatformType platformType, String identifier) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);
		return JWT.create()
				.withClaim("user_id", userId)
				.withClaim("role", role)
				.withClaim("platform", platformType.name())
				.withClaim("identifier", identifier)
				.withIssuedAt(now)
				.withExpiresAt(validity)
				.sign(Algorithm.HMAC256(secretKey));
	}

	public boolean validateToken(String token) {
		try {
			JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
			return true;
		} catch (TokenExpiredException e) {
			return false;
		} catch (JWTVerificationException e) {
			log.error("토큰 검증 실패, secretKey: {}", secretKey);
			return false;
		}
	}

	public Claim getClaim(String token, String claimName) {
		DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
		return decodedJWT.getClaim(claimName);
	}

	public Authentication getAuthentication(String token) {
		DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
		String identifier = decodedJWT.getClaim("identifier").asString();
		String platformStr = decodedJWT.getClaim("platform").asString();
		PlatformType platformType = PlatformType.valueOf(platformStr);
		PrincipalDetails principalDetails = principalDetailsService.loadUserByUsernameAndPlatform(identifier, platformType);
		return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
	}

	public UsernamePasswordAuthenticationToken createAuthenticationFromToken(String token) {
		return (UsernamePasswordAuthenticationToken) getAuthentication(token);
	}

	/**
	 * 액세스 토큰을 쿠키에 저장합니다.
	 */
	public void saveAccessCookie(HttpServletResponse response, String accessToken) {
		int maxAge = (int) (accessTokenValidityInMilliseconds / 1000);

		ResponseCookie cookie = ResponseCookie.from("AT", accessToken)
				.path("/")
				.domain(cookieDomain)
				.sameSite("None")
				.httpOnly(true)
				.secure(true)
				.maxAge(maxAge)
				.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	/**
	 * 리프레시 토큰을 쿠키에 저장합니다.
	 */
	public void saveRefreshCookie(HttpServletResponse response, String refreshToken) {
		int maxAge = (int) (refreshTokenValidityInMilliseconds / 1000);

		ResponseCookie cookie = ResponseCookie.from("RT", refreshToken)
				.path("/")
				.domain(cookieDomain)
				.sameSite("None")
				.httpOnly(true)
				.secure(true)
				.maxAge(maxAge)
				.build();
		response.addHeader("Set-Cookie", cookie.toString());
	}

	/**
	 * 액세스 토큰이 만료된 경우, 리프레시 토큰을 이용해 새로운 액세스 토큰을 발급합니다.
	 */
	public UsernamePasswordAuthenticationToken replaceAccessToken(HttpServletResponse response, String refreshToken)
			throws IOException {
		try {
			// 리프레시 토큰 검증
			DecodedJWT decodedRefresh = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(refreshToken);
			Long userId = decodedRefresh.getClaim("user_id").asLong();
			String role = decodedRefresh.getClaim("role").asString();
			PlatformType platformType = PlatformType.valueOf(decodedRefresh.getClaim("platform").asString());
			String identifier = decodedRefresh.getClaim("identifier").asString();

			// Redis 엔티티 조회 및 리프레시 토큰 일치 여부 확인
			TokenRedis tokenRedis = tokenRedisRepository.findById(String.valueOf(userId))
					.orElseThrow(() -> NotFoundTokenException.EXCEPTION);

			if (!tokenRedis.getRefreshToken().equals(refreshToken)) {
				log.error("리프레시 토큰 불일치: Redis에 저장된 토큰과 요청된 토큰이 다릅니다.");
				throw InvalidTokenException.EXCEPTION;
			}

			log.info("## 토큰 재발급 시작... userId: {}", userId);

			UsernamePasswordAuthenticationToken authentication = createAuthenticationFromToken(refreshToken);
			// 새 액세스 토큰 발급
			String newAccessToken = createAccessToken(userId, role, platformType, identifier);

			// 쿠키에 새 액세스 토큰 저장
			saveAccessCookie(response, newAccessToken);
			// Redis 업데이트
			PartialUpdate<TokenRedis> update = new PartialUpdate<>(String.valueOf(userId), TokenRedis.class).set("accessToken", newAccessToken);
            redisKeyValueTemplate.update(update);

			log.info("## 토큰 재발급 완료. userId: {}", userId);

			return authentication;
		} catch (JWTVerificationException e) {
			log.error("리프레시 토큰 검증 실패: {}", e.getMessage());
			throw InvalidTokenException.EXCEPTION;
		} catch (NotFoundTokenException e) {
			log.error("Redis 토큰 조회 실패: {}", e.getMessage());
			throw NotFoundTokenException.EXCEPTION;
		} catch (RedisException redisException) {
			log.error("Redis 서버 에러: {}", redisException.getMessage());
			throw RedisServerException.EXCEPTION;
		}
	}

	/**
	 * 액세스 토큰을 블랙리스트에 등록합니다.
	 * @param accessToken 블랙리스트에 등록할 액세스 토큰
	 */
	public void addAccessTokenToBlacklist(String accessToken) {
		try {
			DecodedJWT decoded = JWT.decode(accessToken);
			long expiresAt = decoded.getExpiresAt().getTime();
			long now = System.currentTimeMillis();
			long ttlSeconds = (expiresAt - now) / 1000;
			if(ttlSeconds > 0) {
				String blacklistKey = "blacklist:" + accessToken;
				redisStringTemplate.opsForValue().set(blacklistKey, "true", ttlSeconds, TimeUnit.SECONDS);
				log.info("액세스 토큰을 블랙리스트에 등록: {} (TTL: {}초)", accessToken, ttlSeconds);
			}
		} catch (Exception e) {
			log.error("액세스 토큰 블랙리스트 등록 실패", e);
		}
	}
	
	public boolean isTokenBlacklisted(String token) {
		String blacklistKey = "blacklist:" + token;
		return redisStringTemplate.hasKey(blacklistKey);
	}

	public Long getUserIdFromToken(String token) {
		DecodedJWT decoded = JWT.decode(token);
		return decoded.getClaim("user_id").asLong();
	}
}
