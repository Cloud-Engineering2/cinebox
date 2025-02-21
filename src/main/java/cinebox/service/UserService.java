package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cinebox.common.exception.user.DuplicateUserException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.dto.request.AuthRequest;
import cinebox.dto.request.UserRequest;
import cinebox.dto.response.AuthResponse;
import cinebox.dto.response.UserResponse;
import cinebox.entity.User;
import cinebox.repository.UserRepository;
import cinebox.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final AuthenticationManager authenticationManager;
	
    public UserResponse signup(UserRequest user) {
        if (userRepository.existsByIdentifier(user.getIdentifier())) {
            throw DuplicateUserException.EXCEPTION;
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
		User newUser = User.of(user);

		userRepository.save(newUser);
		return UserResponse.from(newUser);
	}

    // 유저 인증 (identifier, pw) -> 토큰 생성 (user_id, role)
	public AuthResponse login(AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getIdentifier(), authRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!userRepository.existsByIdentifier(userDetails.getUsername())) {
            throw NotFoundUserException.EXCEPTION;
        }
        
        User user = userRepository.findByIdentifier(userDetails.getUsername());
        String token = jwtTokenProvider.createToken(user.getUserId(), user.getRole().toString());

        return new AuthResponse(user.getUserId(), user.getIdentifier(), user.getRole().toString(), token);
	}

	public List<UserResponse> getAllUser() {
		return userRepository.findAll().stream().map(UserResponse::from).collect(Collectors.toList());
	}

    public UserResponse getUserById(Long userId) {
    	User user = userRepository.findById(userId).orElseThrow(() -> NotFoundUserException.EXCEPTION);
        return UserResponse.from(user);
    }

	public User updateUser(UserRequest userRequest) {
        if(userRepository.existsByUserId(userRequest.getUserId())) {
        	User updateUser = User.of(userRequest);
        	User user = userRepository.save(updateUser);
        	return user;
        }else {
        	throw NotFoundUserException.EXCEPTION;
        }
	}

	public void deleteUser(Long userId) {
		//본인 인지 확인을 한 후에 
        if (userRepository.existsByUserId(userId)) {
        	userRepository.deleteById(userId);
        }
	}
}
