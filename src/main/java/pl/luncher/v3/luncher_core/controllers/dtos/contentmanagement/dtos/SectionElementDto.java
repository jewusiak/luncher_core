package pl.luncher.v3.luncher_core.controllers.dtos.contentmanagement.dtos;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.contentmanagement.model.ElementType;

/**
 * DTO for {@link pl.luncher.v3.luncher_core.contentmanagement.model.SectionElement}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionElementDto implements Serializable {
  
  private String sourceElementId;
  private ElementType elementType;
  private String header;
  private String subheader;
  private String uri;
  private String thumbnailAccessUrl;
  private String thumbnailId;
}
