package pl.luncher.v3.luncher_core.place.domainservices;

import java.util.List;
import java.util.UUID;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.user.model.User;

public interface PlaceManagementService {

  Place getPlace(UUID uuid, User requestingUser);

  List<Place> searchPlaces(PlaceSearchCommand request);

  Place updatePlace(Place place, List<UUID> imageIds);

  List<Asset> fetchNewImages(List<UUID> imageIds, List<Asset> oldImages);

  Place createPlace(Place place);
}
