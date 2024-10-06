package pl.luncher.v3.luncher_core.controllers.dtos.assets.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class CreateAssetRequest {

  private String name;
  private String description;
  @NotBlank
  private String fileExtension;
  @NotBlank
  @UUID
  private String placeId;

}
