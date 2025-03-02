package pl.luncher.v3.luncher_core.place.model.menus;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.luncher.v3.luncher_core.common.interfaces.Validatable;
import pl.luncher.v3.luncher_core.common.model.MonetaryAmount;
import pl.luncher.v3.luncher_core.common.model.timing.LocalDateTimeRange;
import pl.luncher.v3.luncher_core.common.model.timing.TimeRange;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTimeRange;

@Slf4j
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
        .map(tr -> tr.getSoonestOccurrence(at))
        .peek(so->log.info("Soonest occurrence: {}", so))
        .filter(Objects::nonNull)
        .min(LocalDateTime::compareTo)
        .orElse(null);
  }

  public LocalDateTimeRange getThisOrNextServingRange(LocalDateTime at) {
    return Stream.concat(Stream.ofNullable(oneTimeServingRanges),
            Stream.ofNullable(recurringServingRanges)).flatMap(List::stream)
        .map(tr -> new HashMap<>() {{
          put("e", tr);
          LocalDateTime soonestOccurrence = tr.getSoonestOccurrence(at);
          if (soonestOccurrence != null) {
            put("so", soonestOccurrence);
          }
        }}).filter(m -> m.containsKey("so"))
        .min(Comparator.comparing(m -> (LocalDateTime) m.get("so")))
        .map(m -> ((TimeRange) m.get("e")).getThisOrNextOccurrence(at)).orElse(null);
  }
}
