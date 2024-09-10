package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.And;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;

public class ResponseAssertionsSteps {

  @And("HTTP Response has a list of size {int} in path {}")
  public void responseHasAListOfSizeInPathResults(int size, String path) {
    List<?> list = ParentSteps.getCachedHttpResp().jsonPath().getList(path);

    Assertions.assertThat(list).hasSize(size);
  }

  @And("HTTP Response is:")
  public void responseOfClassPlaceSearchResponseInPathResultsIs(
      List<Map<String, String>> expected) {
    var resp = ParentSteps.getCachedHttpResp().jsonPath();
    for (String key : expected.get(0).keySet()) {
      String expectedAfterIdReplacement = ParentSteps.replaceIds(expected.get(0).get(key));
      Assertions.assertThat(resp.getString(key)).isEqualTo(expectedAfterIdReplacement);
    }
  }

  @And("HTTP Response contains:")
  public void responseContains(List<String> expected) {
    var resp = ParentSteps.getCachedHttpResp().asString();
    for (String expVal : expected) {
      Assertions.assertThat(resp).contains(ParentSteps.replaceIds(expVal));
    }
  }
}
