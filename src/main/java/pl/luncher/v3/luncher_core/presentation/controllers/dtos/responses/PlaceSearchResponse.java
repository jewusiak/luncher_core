package pl.luncher.v3.luncher_core.presentation.controllers.dtos.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.presentation.controllers.dtos.place.responses.PlaceFullResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceSearchResponse {

  List<PlaceFullResponse> results;
}
