package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.And;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;

@Slf4j
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
      String actual = resp.getString(key);
      log.trace("Checking path {}\nexpected: {}\nactual: {}", key, expectedAfterIdReplacement, actual);
      Assertions.assertThat(actual).isEqualTo(expectedAfterIdReplacement);
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
