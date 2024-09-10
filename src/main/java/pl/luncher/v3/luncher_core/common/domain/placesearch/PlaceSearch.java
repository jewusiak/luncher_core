package pl.luncher.v3.luncher_core.common.domain.placesearch;

import java.util.List;
import pl.luncher.v3.luncher_core.common.domain.placesearch.dtos.PlaceSearchDto;

public interface PlaceSearch {

  List<PlaceSearchDto> fetch(int page, int size);
}
