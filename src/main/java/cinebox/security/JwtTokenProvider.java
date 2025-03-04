package cinebox.security;

import java.io.IOException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import cinebox.common.exception.auth.NotFoundTokenException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.entity.TokenRedis;
import cinebox.repository.TokenRedisRepository;
import cinebox.repository.UserRepository;
import io.lettuce.core.RedisException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	private final UserRepository userRepository;

	@Value("${security.jwt.secretkey}")
    private String secretKey;
	@Value("${security.jwt.validityInMilliseconds}")
    private long validityInMilliseconds;

//    public String createToken(Long user_id, String role) {
//        return JWT.create()
//                .withClaim("user_id", user_id) 
//                .withClaim("role", role)
//                .withExpiresAt(new Date(System.currentTimeMillis() + validityInMilliseconds))
//                .sign(Algorithm.HMAC256(secretKey));
//    }
//    
//    public String getToken (HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
//    }
    
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return true;
        } catch(TokenExpiredException e) {
        	return false;
        } catch (JWTVerificationException e) {
            System.out.println(secretKey);
            return false;
        }
    }
    
    public Claim getClaim (String token, String Claim) {
    	DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
    	return decodedJWT.getClaim(Claim);
    }
    
    public Authentication getAuthentication(String token) {
    	DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);

        Long userId = decodedJWT.getClaim("user_id").asLong();
        cinebox.entity.User user = userRepository.findByUserIdAndIsDeletedFalse(userId).orElseThrow(() -> NotFoundUserException.EXCEPTION);
        String role = decodedJWT.getClaim("role").asString();
        
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }
    
    
    
    
    
    
    @Value("${security.jwt.accessTokenValidityInMilliseconds}")
    public long accessTokenValidityInMilliseconds;
    
    @Value("${security.jwt.refreshTokenValidityInMilliseconds}")
    public long refreshTokenValidityInMilliseconds;
    
    public String createAccessToken(Long userId, String role) {
    	Date now = new Date();
    	Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);
    	return JWT.create()
    			.withClaim("user_id", userId)
    			.withClaim("role", role)
    			.withIssuedAt(now)
    			.withExpiresAt(validity)
    			.sign(Algorithm.HMAC256(secretKey));
    }
    
    public String createRefreshToken(Long userId, String role) {
    	Date now = new Date();
    	Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);
    	return JWT.create()
    			.withClaim("user_id", userId)
    			.withClaim("role", role)
    			.withIssuedAt(now)
    			.withExpiresAt(validity)
    			.sign(Algorithm.HMAC256(secretKey));
    }
    
    private final PrincipalDetailsService principalDetailsService;
    private final TokenRedisRepository tokenRedisRepository;
    
    public UsernamePasswordAuthenticationToken createAuthenticationFromToken(String token) {
    	Authentication authentication = getAuthentication(token);
    	UserDetails userDetails = principalDetailsService.loadUserByUsername(authentication.getName());
    	
    	return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
    
    public void saveCookie(HttpServletResponse response, String accessToken) {
    	Cookie cookie = new Cookie("AT", accessToken);
    	cookie.setHttpOnly(true);
    	cookie.setPath("/");
    	cookie.setMaxAge((int)(accessTokenValidityInMilliseconds / 1000));
    	response.addCookie(cookie);
    }
    
    public UsernamePasswordAuthenticationToken replaceAccessToken(HttpServletResponse response, String refreshToken) throws IOException {
        try {
            // Refresh Token 검증
            DecodedJWT decodedRefresh = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(refreshToken);
            Long userId = decodedRefresh.getClaim("user_id").asLong();
            String role = decodedRefresh.getClaim("role").asString();
            
            // 새 Access Token 생성
            String newAccessToken = createAccessToken(userId, role);
            // 쿠키에 저장
            saveCookie(response, newAccessToken);
            
            // Redis에 저장된 토큰 업데이트 (사용자 ID를 key로 저장했다고 가정)
            TokenRedis tokenRedis = tokenRedisRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> NotFoundTokenException.EXCEPTION);
            tokenRedis.updateAccessToken(newAccessToken);
            tokenRedisRepository.save(tokenRedis);
            
            return createAuthenticationFromToken(newAccessToken);
        } catch (TokenExpiredException | NotFoundTokenException e) {
            response.sendRedirect("/error");
        } catch (RedisException redisException) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Redis 서버 에러");
        }
        return null;
    }
}
