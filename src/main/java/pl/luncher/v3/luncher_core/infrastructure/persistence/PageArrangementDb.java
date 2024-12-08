package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Entity
@Table(name = "page_arrangements", schema = "luncher_core")
class PageArrangementDb implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotNull
  private Boolean primaryPage;

  @OneToMany(mappedBy = "pageArrangement", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("listIndex")
  private List<SectionDb> sections;
}
