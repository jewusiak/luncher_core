package pl.luncher.v3.luncher_core.contentmanagement.model;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageArrangement {

  private UUID id;
  private boolean primary;
  private List<Section> sections;

  public void validate() {
    if (sections != null) {
      sections.forEach(Section::validate);
    }
  }
}
