package pl.luncher.v3.luncher_core.place.domainservices;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.luncher.v3.luncher_core.common.model.timing.WeekDayTime;
import pl.luncher.v3.luncher_core.place.model.LocationWithRadius;
import pl.luncher.v3.luncher_core.user.model.User;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceSearchCommand {

  private String textQuery;
  private String placeTypeIdentifier;
  private WeekDayTime openAt;
  @Valid
  private LocationWithRadius location;
  private LocalDateTime hasLunchServedAt;
  private User requestingUser;
  private User owner;
  private String ownerEmail;
  private Boolean enabled;
  @NotNull
  private Integer page;
  @NotNull
  private Integer size;

}
