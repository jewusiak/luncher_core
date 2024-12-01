package pl.luncher.v3.luncher_core.contentmanagement.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section {

  private String sectionHeader;
  private String sectionSubheader;
  private List<SectionElement> sectionElements;

  public void validate() {
    if (sectionElements != null) {
      sectionElements.forEach(SectionElement::validate);
    }
  }
}
