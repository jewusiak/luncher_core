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
public class Section {

  private UUID id;
  private String sectionHeader;
  private String sectionSubheader;
  private List<SectionElement> sectionElements;

  public void validate() {
    if (sectionElements != null) {
      sectionElements.forEach(SectionElement::validate);
    }
  }
}
