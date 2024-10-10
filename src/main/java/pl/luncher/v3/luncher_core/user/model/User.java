package pl.luncher.v3.luncher_core.user.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.luncher.v3.luncher_core.user.domainservices.UserPermissionsChecker;


@Data
@Slf4j
public class User implements UserDetails {

  private UUID uuid;
  private String email;
  private String firstName;
  private String surname;
  private String passwordHash;
  private AppRole role;
  @Getter(AccessLevel.NONE)
  private boolean enabled;


  public void validate() {
    log.info("User is being validated...");
  }

  public UserPermissionsChecker permissions() {
    return new UserPermissionsChecker(this);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    var roles = getRole();
    if (roles == null) {
      roles = AppRole.USER;
    }
    return List.of(roles.getAuthorityObj());
  }

  @Override
  public String getPassword() {
    return getPasswordHash();
  }

  @Override
  public String getUsername() {
    return getEmail();
  }


  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    //todo: implement locking the account
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj instanceof User user
        && user.getUuid() != null) {
      return user.getUuid().equals(this.getUuid());
    }

    return false;
  }
}
