package pl.luncher.v3.luncher_core.application.controllers.dtos.contentmanagement.dtos;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.contentmanagement.model.Section}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionDto implements Serializable {

  private String sectionHeader;
  private String sectionSubheader;
  private List<SectionElementDto> sectionElements;
}