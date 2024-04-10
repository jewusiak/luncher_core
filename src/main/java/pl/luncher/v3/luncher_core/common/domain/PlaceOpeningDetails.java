package pl.luncher.v3.luncher_core.common.domain;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOpeningDetails {
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
//    @JoinTable(schema = "luncher_core", name = "place_standard_opening_times")
    private List<OpeningWindow> standardOpeningTimes;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
//    @JoinTable(schema = "luncher_core", name = "place_opening_exceptions")
    private List<PlaceOpeningException> openingExceptions;
}
