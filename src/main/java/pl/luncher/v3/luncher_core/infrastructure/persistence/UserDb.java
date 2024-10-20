package pl.luncher.v3.luncher_core.infrastructure.persistence;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import pl.luncher.v3.luncher_core.user.model.AppRole;

@Data
@Builder
@Entity
@Table(name = "users_table", schema = "luncher_core")
@Hidden
@NoArgsConstructor
@AllArgsConstructor
@Indexed(index = "user")
class UserDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @GenericField
  private UUID uuid;

  @Column(unique = true)
  @FullTextField(name = "email", analyzer = "email")
  private String email;

  @FullTextField(name = "firstname")
  private String firstName;

  @FullTextField(name = "surname")
  private String surname;

  private String passwordHash;

  //    @GenericField(name = "role")
  private AppRole role;

  private boolean enabled = true;
}
