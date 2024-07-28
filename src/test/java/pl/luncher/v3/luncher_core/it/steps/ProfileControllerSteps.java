package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.And;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import pl.luncher.v3.luncher_core.common.model.responses.UserProfileResponse;

public class ProfileControllerSteps {

  @And("GET profile of authenticated user is:")
  public void getProfileOfAuthenticatedUserIs(List<Map<String, String>> data) {
    var resp = ParentSteps.givenHttpRequest().when().get("profile").thenReturn();
    
    var actual = ParentSteps.getResponseBody(resp, UserProfileResponse.class);
    var expected = ParentSteps.castMapWithErrorHandling(data.get(0), UserProfileResponse.class, resp.getStatusCode());

    Assertions.assertThat(actual).usingRecursiveComparison().ignoringCollectionOrder()
        .ignoringExpectedNullFields()
        .isEqualTo(expected);
  }
}
