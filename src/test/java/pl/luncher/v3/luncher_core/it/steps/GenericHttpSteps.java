package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericHttpSteps {


  @When("Send {} request to {} without body")
  public void sendHttpRequest(String type, String path) {
    sendHttpRequest(type, path, null);
  }
  

  @When("Send {} request to {} with body as below:")
  public void sendHttpRequest(String type, String path, String body) {
    path = ParentSteps.replaceIds(path);
    if (body != null) {
      body = ParentSteps.replaceIds(body);
    }

    log.info("{} {}, body \n{}", type, path, body);

    var req = ParentSteps.givenHttpRequest();
    if (body != null && !body.isBlank()) {
      req.body(body);
    }
    var resp = switch (type) {
      case "POST" -> req.when().post(path);
      case "PUT" -> req.when().put(path);
      case "DELETE" -> req.when().delete(path);
      case "PATCH" -> req.when().patch(path);
      case "GET" -> req.when().get(path);
      default -> throw new IllegalArgumentException("Unknown request type %s".formatted(type));
    };

    ParentSteps.saveHttpResp(resp.thenReturn());
  }
}
