package cinebox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cinebox.common.exception.user.DuplicateUserException;
import cinebox.common.exception.user.NotFoundUserException;
import cinebox.dto.UserDTO;
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

    public User signup(UserDTO userDTO) throws IllegalStateException {
        System.out.println("🔥 회원 가입 로직 실행됨!");
        
        if (userRepository.existsByIdentifier(userDTO.getIdentifier())) {
            throw DuplicateUserException.EXCEPTION;
        }
        
        // password 인코딩 해서 저장 -> 로그인 시 디코딩 해서 확인해야 함
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);
		User user = User.of(userDTO);

		userRepository.save(user);
		return user;
	}

    // 유저 인증 (identifier, pw) -> 토큰 생성 (user_id, role)
	public String login(UserDTO userDTO) {

        try {
        	
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getIdentifier(), userDTO.getPassword())
            );
            
            System.out.println("✅ 인증 성공!");

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByIdentifier(userDetails.getUsername())
                    .orElseThrow(() -> NotFoundUserException.EXCEPTION);

            return jwtTokenProvider.createToken(user.getUserId(), user.getRole().toString());

        } catch (BadCredentialsException e) {
                throw new BadCredentialsException(e.getMessage());
        }
	}
	

	public List<UserDTO> getAllUser() {
		return userRepository.findAll().stream().map(UserDTO::from).collect(Collectors.toList());
	}

    public UserDTO getUserById(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> NotFoundUserException.EXCEPTION);

        return UserDTO.from(user);
    }

	public void updateUser(UserDTO userDTO) {
        User user = userRepository.findByUserId(userDTO.getUserId())
                .orElseThrow(() -> NotFoundUserException.EXCEPTION);
        
        if(userRepository.existsByUserId(userDTO.getUserId())) {
        	User updatedUser = User.of(userDTO);
        	userRepository.save(updatedUser);
        }
	}

	public void deleteUser(Long userId) {
        if (userRepository.existsByUserId(userId)) {
        	userRepository.deleteById(userId);
        }
	}

}
