package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.luncher.v3.luncher_core.contentmanagement.model.Section;

/**
 * DTO for {@link Section}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "page_arrangement_sections", schema = "luncher_core")
@ToString(exclude = {"sectionElements", "pageArrangement"})
class SectionDb implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private int listIndex;
  private String sectionHeader;
  private String sectionSubheader;

  @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("listIndex")
  private List<SectionElementDb> sectionElements;

  @ManyToOne(optional = false)
  private PageArrangementDb pageArrangement;
}