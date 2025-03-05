package cinebox.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cinebox.common.exception.user.DuplicatedIdentifierException;
import cinebox.common.enums.Role;
import cinebox.common.exception.user.DuplicatedEmailException;
import cinebox.common.exception.user.DuplicatedPhoneException;
import cinebox.common.exception.user.DuplicatedFieldException;
import cinebox.dto.request.AuthRequest;
import cinebox.dto.request.SignUpRequest;
import cinebox.dto.response.AuthResponse;
import cinebox.dto.response.UserResponse;
import cinebox.entity.TokenRedis;
import cinebox.entity.User;
import cinebox.repository.TokenRedisRepository;
import cinebox.repository.UserRepository;
import cinebox.security.JwtTokenProvider;
import cinebox.security.PrincipalDetails;
import cinebox.security.SecurityUtil;
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
	public UserResponse signup(SignUpRequest request) {
		if (userRepository.existsByIdentifier(request.identifier())) {
			throw DuplicatedIdentifierException.EXCEPTION;
		}
		if (userRepository.existsByEmail(request.email())) {
			throw DuplicatedEmailException.EXCEPTION;
		}
		if (userRepository.existsByPhone(request.phone())) {
			throw DuplicatedPhoneException.EXCEPTION;
		}

        String encodedPassword = passwordEncoder.encode(request.password());
		User newUser = User.createUser(request, encodedPassword);

		// ADMIN이 생성하는 계정이 아니라면 USER로 역할 고정
		try {
			if (SecurityUtil.getCurrentUser() != null && SecurityUtil.isAdmin()) {
				newUser.updateUserRole(Role.USER);
			}
		} catch(Exception e) {
			newUser.updateUserRole(Role.USER);
		}

		try {
			User savedUser = userRepository.save(newUser);
			return UserResponse.from(savedUser);
		} catch(DataIntegrityViolationException e) {
			throw DuplicatedFieldException.EXCEPTION;
		}
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

		jwtTokenProvider.saveAccessCookie(response, accessToken);
		jwtTokenProvider.saveRefreshCookie(response, refreshToken);
		return new AuthResponse(user.getUserId(), user.getRole().toString(), user.getIdentifier());
	}
}
