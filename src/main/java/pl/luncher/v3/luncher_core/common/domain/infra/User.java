package pl.luncher.v3.luncher_core.common.domain.infra;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "users", schema = "luncher_core")
@Hidden
@NoArgsConstructor
@AllArgsConstructor
@Indexed(index = "user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    // postgres specific?
    @Column(columnDefinition = "serial", updatable = false, insertable = false)
    @JsonIgnore
    @Generated
    private int sequence;

    @FullTextField(name = "email", analyzer = "email")
    @Column(unique = true)
    private String email;

    @FullTextField(name = "firstname")
    @Analyzer
    private String firstName;

    @FullTextField(name = "surname")
    @Analyzer
    private String surname;

    @Getter(AccessLevel.NONE)
    private String passwordHash;

    //    @GenericField(name = "role")
    private AppRole role;

    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var roles = getRole();
        if (roles == null) roles = AppRole.USER;
        return List.of(roles.authorityObj());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
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
        return this.enabled;
    }
}
