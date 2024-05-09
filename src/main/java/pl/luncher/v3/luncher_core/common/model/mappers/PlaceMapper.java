package pl.luncher.v3.luncher_core.common.model.mappers;

import org.mapstruct.Mapper;
import pl.luncher.v3.luncher_core.common.domain.Place;
import pl.luncher.v3.luncher_core.common.model.requests.CreatePlaceRequest;

@Mapper(componentModel = "spring")
public interface PlaceMapper {

  Place map(CreatePlaceRequest request);

}
