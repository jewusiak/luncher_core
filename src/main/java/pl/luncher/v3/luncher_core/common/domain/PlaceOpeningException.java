package pl.luncher.v3.luncher_core.common.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
