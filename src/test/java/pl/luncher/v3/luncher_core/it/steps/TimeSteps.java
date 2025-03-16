package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.And;
import java.time.LocalDateTime;
import pl.luncher.v3.luncher_core.common.services.LocalDateTimeProviderMock;

public class TimeSteps {

  @And("Simulated time is {}")
  public void simulatedTimeIsT(String time) {
    LocalDateTimeProviderMock.mockTime(LocalDateTime.parse(time));
  }
}
