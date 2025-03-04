package cinebox.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.user.DuplicateUserException;
import cinebox.dto.request.AuthRequest;
import cinebox.dto.request.UserRequest;
import cinebox.dto.response.AuthResponse;
import cinebox.dto.response.UserResponse;
import cinebox.entity.TokenRedis;
import cinebox.entity.User;
import cinebox.repository.TokenRedisRepository;
import cinebox.repository.UserRepository;
import cinebox.security.JwtTokenProvider;
import cinebox.security.PrincipalDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	private final TokenRedisRepository tokenRedisRepository;

	@Override
	@Transactional
	public UserResponse signup(UserRequest request) {
		boolean isDuplicatedIdentifier = userRepository.findByIdentifier(request.getIdentifier()).isPresent();
        if(isDuplicatedIdentifier) {
        	throw DuplicateUserException.EXCEPTION;
        }
        
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
		User newUser = User.of(request);

		userRepository.save(newUser);
		return UserResponse.from(newUser);
	}

	@Override
	@Transactional
	public AuthResponse login(AuthRequest request, HttpServletResponse response) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		User user = principal.getUser();
		
		String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), user.getRole().name());
		String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), user.getRole().name());
		
		TokenRedis tokenRedis = new TokenRedis(String.valueOf(user.getUserId()), accessToken, refreshToken);
		tokenRedisRepository.save(tokenRedis);

		jwtTokenProvider.saveCookie(response, accessToken);
		return new AuthResponse(accessToken, refreshToken);
	}

}
