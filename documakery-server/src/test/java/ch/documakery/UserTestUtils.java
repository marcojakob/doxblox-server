package ch.documakery;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import ch.documakery.domain.user.SecurityRole;
import ch.documakery.domain.user.User;

/**
 * Rtility class used for user related testing.
 * 
 * @author Marco Jakob
 */
public class UserTestUtils {
  
  public static final GrantedAuthority AUTHORITY = new SimpleGrantedAuthority(
      SecurityRole.ROLE_USER.name());
  public static final String EMAIL = "test@user.com";
  public static final String PASSWORD = "testUserPassword";
  public static final String NICKNAME = "testUserNickname";
  
  public static final User TEST_USER;
  
  static {
    // initialize the TEST_USER
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(AUTHORITY);
    
    TEST_USER = new User(EMAIL, PASSWORD, authorities);
    TEST_USER.setNickname(NICKNAME);
  }
}
