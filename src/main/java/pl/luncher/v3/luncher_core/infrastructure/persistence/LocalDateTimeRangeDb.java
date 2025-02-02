package pl.luncher.v3.luncher_core.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  private MenuOfferDb menuOffer;
}
