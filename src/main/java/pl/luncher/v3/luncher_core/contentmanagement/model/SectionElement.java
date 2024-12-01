package pl.luncher.v3.luncher_core.contentmanagement.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionElement {

  private String sourceElementId;
  private ElementType elementType;
  private String heading;
  private String subheading;
  private String uri;

  public void validate() {
    switch (elementType) {
      case PLACE:
      case PLACE_TYPE:
        if (sourceElementId == null) {
          throw new IllegalArgumentException(
              "Section Element of type %s has to have a sourceElementId.".formatted(
                  elementType.name()));
        }
        break;
    }
  }
}
