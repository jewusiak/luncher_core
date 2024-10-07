package pl.luncher.v3.luncher_core.user.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum AppRole {

  // Sequence here defines role hierarchy! ... > ... > ... > ...
  SYS_ROOT, SYS_ADMIN, SYS_MOD, REST_MANAGER, USER;

  public static AppRole getHighestRole() {
    return AppRole.values()[0];
  }

  /**
   * Implements reversed compareTo method, so that this reflects role hierarchy
   *
   * @param other other role
   * @return 0 if equal, positive if this > other , negative if this < other
   */
  public int compareRoleTo(AppRole other) {
    return other.ordinal() - this.ordinal();
  }

  public String authorityName() {
    return "ROLE_" + this.name();
  }

  public SimpleGrantedAuthority getAuthorityObj() {
    return new SimpleGrantedAuthority(authorityName());
  }

  public static class hasRole {

    public final static String SYS_ROOT = "hasAuthority('ROLE_SYS_ROOT')";
    public final static String SYS_ADMIN = "hasAuthority('ROLE_SYS_ADMIN')";
    public final static String SYS_MOD = "hasAuthority('ROLE_SYS_MOD')";
    public final static String REST_MANAGER = "hasAuthority('ROLE_REST_MANAGER')";
    public final static String USER = "hasAuthority('ROLE_USER')";
  }
}
