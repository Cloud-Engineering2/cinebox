package cinebox.security;

import java.util.Collections;
import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import cinebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	private final UserRepository userRepository;

    private final String secretKey = "your-secret-key-your-secret-key"; // 🔹 256비트 이상 추천
    private final long validityInMilliseconds = 3600000; // 1시간

    public String createToken(Long user_id, String role) {
        return JWT.create()
                .withClaim("user_id", user_id) 
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .sign(Algorithm.HMAC256(secretKey));
    }
    
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            System.out.println(secretKey);
            return false;
        }
    }
    
    public Authentication getAuthentication(String token) {
    	DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        Long userId = decodedJWT.getClaim("user_id").asLong();
        cinebox.entity.User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원 정보를 찾을 수 없습니다."));
        String role = decodedJWT.getClaim("role").asString(); // 🔥 역할 가져오기
        
        User userDetails = new User(user.getIdentifier(), "", Collections.singleton(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
