package pl.luncher.v3.luncher_core.presentation.controllers.dtos.place.responses;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.dto.PlaceTypeDto;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.place.model.Place}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceBasicResponse implements Serializable {

  private UUID id;
  private String name;
  private String longName;
  private PlaceTypeDto placeType;
}
