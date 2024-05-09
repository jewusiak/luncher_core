package pl.luncher.v3.luncher_core.common.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "place_opening_exceptions", schema = "luncher_core")
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOpeningException {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID uuid;

  private LocalDate date;

  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
//    @JoinTable(schema = "luncher_core")
  private List<OpeningWindow> openingWindows;
  private String description;

//    @ManyToOne
//    private Place place;
}
