package ch.documakery;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
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
  
  public static final GrantedAuthority TEST_USER_AUTHORITY = new SimpleGrantedAuthority(
      SecurityRole.ROLE_USER.name());
  public static final ObjectId TEST_USER_ID = new ObjectId("51255ec818a38d4e9c179b29");
  public static final String TEST_USER_EMAIL = "test@user.com";
  public static final String TEST_USER_PASSWORD = "testUserPassword";
  public static final String TEST_USER_NICKNAME = "testUserNickname";
  
  public static final User TEST_USER;
  
  static {
    // initialize the TEST_USER
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    authorities.add(TEST_USER_AUTHORITY);
    
    TEST_USER = new User(TEST_USER_EMAIL, TEST_USER_PASSWORD, authorities);
    TEST_USER.setNickname(TEST_USER_NICKNAME);
    TEST_USER.setId(TEST_USER_ID);
  }
}
