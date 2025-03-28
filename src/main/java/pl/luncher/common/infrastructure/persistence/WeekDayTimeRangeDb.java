package pl.luncher.common.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;

@Entity
@Table(name = "opening_windows", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"place", "menuOffer"})
class WeekDayTimeRangeDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  //todo: introduce system which takes into account opening windows tue 22:00 - wed 02:00, and sun 22:00 - mon 02:00
  // for now - tue 22:00 - wed 02:00 => two windows: tue 22:00-23:59 and wed 00:00-02:00

  /* Seconds since Monday 00:00:00 = 0
   * max is Sunday 23:59:59 = 604799
   * in case opening time overlaps - do % 604800:
   * Monday 00:10:00 can be represented as both:
   * 10x60 = 600, or 7 days+10 mins = 7x24x60x60 + 10x60 = 605400 % 604800 = 600
   */
  @GenericField
  private int startTime;
  @GenericField
  private int endTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "place_opening_windows",
      schema = "luncher_core", inverseJoinColumns = @JoinColumn(name = "place_id", referencedColumnName = "id"), joinColumns = @JoinColumn(name = "weekdaytimerange_id", referencedColumnName = "id"))
  private PlaceDb place;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(name = "menu_offer_recurring_serving_ranges", schema = "luncher_core", inverseJoinColumns = @JoinColumn(name = "menu_offer_id", referencedColumnName = "id"), joinColumns = @JoinColumn(name = "weekdaytimerange_id", referencedColumnName = "id"))
  private MenuOfferDb menuOffer;
}
