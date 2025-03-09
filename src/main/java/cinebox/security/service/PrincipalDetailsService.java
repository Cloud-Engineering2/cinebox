package cinebox.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cinebox.common.exception.user.NotFoundUserException;
import cinebox.domain.user.entity.User;
import cinebox.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    	User user = userRepository.findByIdentifier(username).orElseThrow(() -> NotFoundUserException.EXCEPTION);
        return new PrincipalDetails (user);
    }

}
