package pl.luncher.v3.luncher_core.controllers.dtos.assets.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class CreateAssetRequest {

  private String description;
  @NotBlank
  @UUID
  private String placeId;

}
