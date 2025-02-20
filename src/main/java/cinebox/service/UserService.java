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
	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;

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
        
        System.out.println("✅ 인증 성공!");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByIdentifier(userDetails.getUsername());
        String token = jwtTokenProvider.createToken(user.getUserId(), user.getRole().toString());

        return new AuthResponse (user.getUserId(), user.getIdentifier(), user.getRole().toString(), token);
	}
	

	public List<UserResponse> getAllUser() {
		return userRepository.findAll().stream().map(UserResponse::from).collect(Collectors.toList());
	}

    public UserResponse getUserById(Long userId) {
        User user = userRepository.findByUserId(userId);
        return UserResponse.from(user);
    }

	public void updateUser(UserRequest user) {
        if(userRepository.existsByUserId(user.getUserId())) {
        	User updatedUser = User.of(user);
        	userRepository.save(updatedUser);
        }
	}

	public void deleteUser(Long userId) {
        if (userRepository.existsByUserId(userId)) {
        	userRepository.deleteById(userId);
        }
	}

}
