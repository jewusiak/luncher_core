package pl.luncher.v3.luncher_core.common.model.requests;

import java.util.Map;
import lombok.Data;
import pl.luncher.v3.luncher_core.common.domain.OpeningWindow;
import pl.luncher.v3.luncher_core.common.domain.valueobjects.Address;

@Data
public class CreatePlaceRequest {

  private String name;
  private String longName;
  private String description;
  private Address address;
  private String googleMapsReference;
  private Map<String, OpeningWindow> standardOpenTimes;
}
