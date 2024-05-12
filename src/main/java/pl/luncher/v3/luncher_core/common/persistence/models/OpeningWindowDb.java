package pl.luncher.v3.luncher_core.common.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "opening_windows", schema = "luncher_core")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Builder(access = AccessLevel.MODULE)
@Getter
@Setter
public class OpeningWindowDb {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  private DayOfWeek dayOfWeek;
  private LocalTime startTime;
  private LocalTime endTime;
  private String description; //optional

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  private PlaceDb place;
}
