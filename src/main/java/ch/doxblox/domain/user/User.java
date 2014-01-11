package ch.doxblox.domain.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;


/**
 * The domain object for user information.
 * 
 * @author Marco Jakob
 */
@Document
public class User implements UserDetails, CredentialsContainer {
  
  private static final long serialVersionUID = 1L;

  @Id
  private ObjectId id;

  /**
   * The (unique) email address. Used as username.
   */
  @Indexed(unique=true)
  private String email; 
  
  /**
   * The confirmation code. May be <code>null</code>.
   */
  private ConfirmationCode emailConfirmationCode;

  /**
   * <code>true</code> if the email address was confirmed. Default is <code>false</code>.
   */
  private boolean emailConfirmed;

  /**
   * The nickname.
   */
  @Indexed(unique=true)
  private String nickname;

  /**
   * The hashed password.
   */
  private String password;

  /**
   * the authorities granted to the user. Each authority is a String representation of a
   * {@link SecurityRole}. Authorities are sorted by natural key.
   */
  private Set<GrantedAuthority> authorities;
  
  /**
   * Indicates whether the user's account has expired. An expired account cannot be authenticated.
   * Default is <code>true</code>.
   */
  private boolean accountNonExpired = true;

  /**
   * Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
   * Default is <code>true</code>.
   */
  private boolean accountNonLocked = true;
  
  /**
   * Indicates whether the user's credentials (password) has expired. Expired credentials prevent
   * authentication. Default is <code>true</code>.
   */
  private boolean credentialsNonExpired = true;
  
  /**
   * Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
   * Default is <code>true</code>.
   */
  private boolean enabled = true;
  
  /**
   * Default constructor for persistence framework.
   */
  @SuppressWarnings("unused")
  private User() {
  }
  
  /**
   * Construct the {@link User} with no authorities.
   * 
   * @param email the email (used as username)
   * @param password the hashed password
   */
  public User(String email, String password) {
    this(email, password, AuthorityUtils.NO_AUTHORITIES);
  }

  /**
   * Construct the {@link User}.
   * 
   * @param email the email (used as username)
   * @param password the hashed password
   * @param authorities the authorities that should be granted to the caller if they presented the
   *          correct email and password and the user is enabled. Authorities must not be
   *          <code>null</code>.
   * 
   * @throws IllegalArgumentException if a <code>null</code> value was passed either as a parameter
   *           or as an element in the <code>GrantedAuthority</code> collection
   */
  public User(String email, String password, Collection<? extends GrantedAuthority> authorities) {
    if (((email == null) || "".equals(email)) || (password == null)) {
      throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
    }

    this.email = email;
    this.password = password;
    this.authorities = sortAuthorities(authorities);
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Note: Email is used as username.
   */
  @Override
  public String getUsername() {
    return email;
  }

  public ConfirmationCode getEmailConfirmationCode() {
    return emailConfirmationCode;
  }

  public void setEmailConfirmationCode(ConfirmationCode emailConfirmationCode) {
    this.emailConfirmationCode = emailConfirmationCode;
  }

  public boolean isEmailConfirmed() {
    return emailConfirmed;
  }

  public void setEmailConfirmed(boolean emailConfirmed) {
    this.emailConfirmed = emailConfirmed;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public Set<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(Set<GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  public void setAccountNonExpired(boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired(boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
  
  public void eraseCredentials() {
    password = null;
  }

  /**
   * Copy from {@link org.springframework.security.core.userdetails.User}.
   * 
   * @param authorities
   * @return
   */
  private static SortedSet<GrantedAuthority> sortAuthorities(
      Collection<? extends GrantedAuthority> authorities) {
    Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
    // Ensure array iteration order is predictable (as per UserDetails.getAuthorities() contract and
    // SEC-717)
    SortedSet<GrantedAuthority> sortedAuthorities =
        new TreeSet<GrantedAuthority>(new AuthorityComparator());

    for (GrantedAuthority grantedAuthority : authorities) {
      Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
      sortedAuthorities.add(grantedAuthority);
    }

    return sortedAuthorities;
  }

  /**
   * Copy from {@link org.springframework.security.core.userdetails.User}.
   * 
   * @param authorities
   * @return
   */
  private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
    private static final long serialVersionUID = 1L;

    public int compare(GrantedAuthority g1, GrantedAuthority g2) {
      // Neither should ever be null as each entry is checked before adding it to the set.
      // If the authority is null, it is a custom authority and should precede others.
      if (g2.getAuthority() == null) {
        return -1;
      }

      if (g1.getAuthority() == null) {
        return 1;
      }

      return g1.getAuthority().compareTo(g2.getAuthority());
    }
  }
  
  /**
   * Returns <code>true</code> if the supplied object is a {@link User} instance with the same email
   * value.
   * <p>
   * In other words, the objects are equal if they have the same email, representing the same
   * principal.
   */
  @Override
  public boolean equals(Object otherUser) {
    if (otherUser instanceof User) {
      return email.equals(((User) otherUser).getEmail());
    }
    return false;
  }

  /**
   * Returns the hashcode of the {@code email}.
   */
  @Override
  public int hashCode() {
    return email.hashCode();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString()).append(": ");
    sb.append("Email: ").append(this.email).append("; ");
    sb.append("Nickname: ").append(this.nickname).append("; ");
    sb.append("Password: [PROTECTED]; ");
    sb.append("EmailConfirmed: ").append(this.emailConfirmed).append("; ");
    sb.append("Enabled: ").append(this.enabled).append("; ");
    sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
    sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
    sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");

    if (!authorities.isEmpty()) {
      sb.append("Granted Authorities: ");

      boolean first = true;
      for (GrantedAuthority auth : authorities) {
        if (!first) {
          sb.append(",");
        }
        first = false;

        sb.append(auth);
      }
    } else {
      sb.append("Not granted any authorities");
    }

    return sb.toString();
  }
}