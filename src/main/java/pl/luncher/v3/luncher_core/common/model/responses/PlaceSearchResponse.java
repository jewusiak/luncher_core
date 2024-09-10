package pl.luncher.v3.luncher_core.common.model.responses;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.domain.placesearch.dtos.PlaceSearchDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceSearchResponse {

  List<PlaceSearchDto> results;
}
