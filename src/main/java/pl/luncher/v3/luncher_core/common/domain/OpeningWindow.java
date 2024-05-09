package pl.luncher.v3.luncher_core.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "opening_windows", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
public class OpeningWindow {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  private DayOfWeek dayOfWeek;
  private LocalTime startTime;
  private LocalTime endTime;
  private String description; //optional

//    @ManyToOne
//    Place place;
//
//    @ManyToOne
//    PlaceOpeningException placeOpeningException;
}
