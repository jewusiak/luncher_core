package pl.luncher.common.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Entity
@Table(name = "local_date_time_ranges", schema = "luncher_core")
@Data
@ToString(exclude = "menuOffer")
class LocalDateTimeRangeDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @GenericField
  private LocalDateTime startTime;

  @GenericField
  private LocalDateTime endTime;

  @ManyToOne
  @JoinTable(name = "menu_offer_onetime_serving_ranges", schema = "luncher_core", inverseJoinColumns = @JoinColumn(name = "menu_offer_id", referencedColumnName = "id"), joinColumns = @JoinColumn(name = "localdatetimerange_id", referencedColumnName = "id"))
  private MenuOfferDb menuOffer;
}
