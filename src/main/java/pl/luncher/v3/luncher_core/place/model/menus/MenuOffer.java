package pl.luncher.v3.luncher_core.place.model.menus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.interfaces.Validatable;
import pl.luncher.v3.luncher_core.common.model.MonetaryAmount;
import pl.luncher.v3.luncher_core.common.model.timing.LocalDateTimeRange;
import pl.luncher.v3.luncher_core.common.model.timing.TimeRange;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTimeRange;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuOffer implements Validatable {


  private UUID id;
  private String name;
  private MonetaryAmount basePrice;
  private List<Part> parts;

  private List<WeekDayTimeRange> recurringServingRanges;
  private List<LocalDateTimeRange> oneTimeServingRanges;

  @Override
  public void validate() {
    parts.forEach(Validatable::validate);
  }

  public boolean isBeingServed(LocalDateTime at) {
    return Stream.concat(recurringServingRanges.stream(), oneTimeServingRanges.stream())
        .anyMatch(tr -> tr.isWithin(at));
  }

  public LocalDateTime getSoonestServingTime(LocalDateTime at) {
    return Stream.concat(Stream.ofNullable(oneTimeServingRanges),
            Stream.ofNullable(recurringServingRanges))
        .flatMap(List::stream)
        .min(Comparator.comparing((TimeRange a) -> a.getSoonestOccurrence(at)))
        .map(t -> t.getSoonestOccurrence(at)).orElse(null);
  }
}
