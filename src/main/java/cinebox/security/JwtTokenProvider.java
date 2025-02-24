package cinebox.security;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	private final UserRepository userRepository;

	@Value("${security.jwt.secretkey}")
    private String secretKey;
	@Value("${security.jwt.validityInMilliseconds}")
    private long validityInMilliseconds;

    public String createToken(Long user_id, String role) {
        return JWT.create()
                .withClaim("user_id", user_id) 
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .sign(Algorithm.HMAC256(secretKey));
    }
    
    public String getToken (HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
    }
    
    public boolean validateToken(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println(secretKey);
            return false;
        }
    }
    
    public Authentication getAuthentication(String token) {
    	DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);

        Long userId = decodedJWT.getClaim("user_id").asLong();
        cinebox.entity.User user = userRepository.findById(userId).orElseThrow(() -> NotFoundUserException.EXCEPTION);
        String role = decodedJWT.getClaim("role").asString();
        
        User userDetails = new User(user.getIdentifier(), "", Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role)));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    
    public void isUserMatchedWithToken (String identifier, String token) {
        UserDetails userDetails = (UserDetails)getAuthentication(token).getPrincipal();

        if (!validateToken(token) || !userDetails.getUsername().equals(identifier)) {
        	throw NoAuthorizedUserException.EXCEPTION;
        }
    }
}
