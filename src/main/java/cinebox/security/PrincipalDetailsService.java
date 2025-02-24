package cinebox.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cinebox.common.exception.user.NotFoundUserException;
import cinebox.entity.User;
import cinebox.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userRepository.existsByIdentifier(username)) {
            throw NotFoundUserException.EXCEPTION;
        }
    	User user = userRepository.findByIdentifier(username);

        return new PrincipalDetails (user);
    }

}
