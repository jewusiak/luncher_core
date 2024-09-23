package pl.luncher.v3.luncher_core.presentation.controllers.dtos.common;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.UserDto}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private UUID uuid;
  private String email;
}