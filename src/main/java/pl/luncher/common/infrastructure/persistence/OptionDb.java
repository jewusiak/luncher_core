package pl.luncher.common.infrastructure.persistence;


import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "menu_options", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PACKAGE)
@Getter
@Setter
@ToString(exclude = {"parentPart"})
class OptionDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String name;
  private String description;
  @Column(columnDefinition = "integer not null default 0")
  private int listIdx;
  @Embedded
  private MonetaryAmountDb supplement;

  @ManyToOne
  @JoinTable(name = "menu_part_options",
      schema = "luncher_core", inverseJoinColumns = @JoinColumn(name = "part_id", referencedColumnName = "id"), joinColumns = @JoinColumn(name = "option_id", referencedColumnName = "id"))
  private PartDb parentPart;
}
