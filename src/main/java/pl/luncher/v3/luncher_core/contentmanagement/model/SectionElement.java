package pl.luncher.v3.luncher_core.contentmanagement.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.assets.model.Asset;
import pl.luncher.v3.luncher_core.place.model.Place;
import pl.luncher.v3.luncher_core.placetype.model.PlaceType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionElement {

  private UUID id;
  private Place place;
  private PlaceType placeType;
  private String header;
  private String subheader;
  private String uri;
  private Asset thumbnail;

  public void validate() {
    if (place == null && placeType == null) {
      throw new IllegalArgumentException("Section Element has to have a source element assigned.");
    }
  }

  public ElementType getElementType() {
    return place != null ? ElementType.PLACE : ElementType.PLACE_TYPE;
  }

  public String getSourceElementId() {
    return place != null && place.getId() != null ? place.getId().toString()
        : placeType.getIdentifier();
  }
}
