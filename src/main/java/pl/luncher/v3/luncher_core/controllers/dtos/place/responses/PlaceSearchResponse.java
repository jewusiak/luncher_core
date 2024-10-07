package pl.luncher.v3.luncher_core.controllers.dtos.place.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceSearchResponse {

  List<PlaceFullResponse> results;
}
