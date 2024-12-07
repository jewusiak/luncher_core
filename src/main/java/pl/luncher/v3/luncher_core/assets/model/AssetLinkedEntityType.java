package pl.luncher.v3.luncher_core.assets.model;

import lombok.Getter;
import pl.luncher.v3.luncher_core.contentmanagement.model.SectionElement;
import pl.luncher.v3.luncher_core.place.model.Place;

@Getter
public enum AssetLinkedEntityType {
  PLACE(Place.class), SECTION_ELEMENT(SectionElement.class);

  private final Class<?> clazz;

  AssetLinkedEntityType(Class<?> clazz) {
    this.clazz = clazz;
  }
}
