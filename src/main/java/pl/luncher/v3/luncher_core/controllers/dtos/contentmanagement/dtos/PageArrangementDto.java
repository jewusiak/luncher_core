package pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.dtos;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.contentmanagement.model.PageArrangement}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageArrangementDto implements Serializable {

  private UUID id;
  private boolean primaryPage;
  private List<SectionDto> sections;
}
