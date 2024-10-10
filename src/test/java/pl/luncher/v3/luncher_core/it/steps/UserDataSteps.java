package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.Given;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import pl.luncher.v3.luncher_core.infrastructure.persistence.UserRepositoryHelper;

@RequiredArgsConstructor
public class UserDataSteps {

  private final UserRepositoryHelper userRepositoryHelper;

  @Given("User(s) exist(s):")
  public void userExists(List<Map<String, String>> data) {
    userRepositoryHelper.saveUsersFromMap(data);
  }

}
