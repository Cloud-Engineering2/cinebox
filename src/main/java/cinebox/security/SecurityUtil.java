package cinebox.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import cinebox.common.exception.user.NoAuthorizedUserException;
import cinebox.domain.user.entity.User;
import cinebox.security.service.PrincipalDetails;

public class SecurityUtil {
	public static User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof PrincipalDetails) {
			return ((PrincipalDetails) auth.getPrincipal()).getUser();
		}
		throw NoAuthorizedUserException.EXCEPTION;
	}
	
	public static boolean isAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof PrincipalDetails) {
		    return auth.getAuthorities().stream()
		    		.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
		}
		return false;
	}
}
