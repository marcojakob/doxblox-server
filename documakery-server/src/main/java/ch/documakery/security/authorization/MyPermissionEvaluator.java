package ch.documakery.security.authorization;
import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import ch.documakery.domain.user.SecurityRole;

public class MyPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean hasPermission = false;

        if (targetDomainObject.equals("Todo")) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String principalRole = getRole(userDetails.getAuthorities());
                if (principalRole.equals(SecurityRole.ROLE_USER.name())) {
                    hasPermission = true;
                }
            }
        }

        return hasPermission;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        //Not required here.
        return false;
    }

    private String getRole(Collection<? extends GrantedAuthority> authorities) {
        return authorities.iterator().next().getAuthority();
    }
}