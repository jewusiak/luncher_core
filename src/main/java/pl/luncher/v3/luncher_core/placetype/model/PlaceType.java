package pl.luncher.v3.luncher_core.placetype.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PlaceType {

  private String identifier;

  private String iconName;

  private String name;

  public void validate() {
    log.info("PlaceType is being validated...");
  }
}
