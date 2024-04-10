package pl.luncher.v3.luncher_core.common.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import pl.luncher.v3.luncher_core.common.domain.infra.User;
import pl.luncher.v3.luncher_core.common.domain.valueobjects.Address;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


@Entity
@Data
@Builder
@NoArgsConstructor
@Table(name = "places", schema = "luncher_core")
@AllArgsConstructor
@Indexed(index = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @FullTextField private String name;
    @FullTextField
    private String longName;
    @FullTextField
    private String description;
    @Embedded
    @IndexedEmbedded
    private Address address; //contains method to generate readable address?? for FE
    private String googleMapsReference;

    @Embedded private PlaceOpeningDetails openingDetails;

    @ManyToOne private User owner;
    
    @ManyToOne
    private PlaceType placeType;

    @ManyToMany
    @JoinTable(schema = "luncher_core")
    private Set<User> allowedUsers;

    public void addOpeningException(PlaceOpeningException exception) {
        openingDetails = openingDetails == null ? new PlaceOpeningDetails() : openingDetails;
        if (openingDetails.getOpeningExceptions() == null) {
            openingDetails.setOpeningExceptions(new ArrayList<>());
        }
        openingDetails.getOpeningExceptions().add(exception);
    }

    public void removeOpeningException(UUID placeOpeningExceptionId) {
        openingDetails.getOpeningExceptions().removeIf(placeOpeningException -> placeOpeningException.getUuid()
                                                                                                     .equals(placeOpeningExceptionId));
    }

    public void addStandardOpeningTime(OpeningWindow openingWindow) {
//        openingWindow.setPlace(this);
        openingDetails = openingDetails == null ? new PlaceOpeningDetails() : openingDetails;
        if (openingDetails.getStandardOpeningTimes() == null) {
            openingDetails.setStandardOpeningTimes(new ArrayList<>());
        }
        openingDetails.getStandardOpeningTimes().add(openingWindow);
    }

    public void removeStandardOpeningTime(UUID openingWindowId) {
        openingDetails.getStandardOpeningTimes()
                      .removeIf(openingWindow -> openingWindow.getUuid().equals(openingWindowId));
    }

}
