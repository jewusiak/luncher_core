package pl.luncher.v3.luncher_core.user.persistence.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "forgot_password_intentions", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ForgottenPasswordIntentDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID requestUuid;
  @NotNull
  private LocalDateTime validityDate;
  @NotNull
  private boolean used;
  
  @ManyToOne
  private UserDb user;
}
