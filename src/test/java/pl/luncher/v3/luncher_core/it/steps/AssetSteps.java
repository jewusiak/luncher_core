package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;

@RequiredArgsConstructor
public class AssetSteps {

  @When("User uploads place image as below ID {}:")
  public void userUploadsPlaceImageAsBelowID(String placeIdx, List<Map<String, String>> map)
      throws IOException {
    var data = ParentSteps.replaceIds(map.get(0));

    var response = ParentSteps.givenHttpRequest().contentType("multipart/form-data")
        .multiPart("file", new ClassPathResource(data.get("filePath")).getFile())
        .queryParam("description", data.get("description")).when()
        .post("/place/%s/images".formatted(data.get("placeUuid"))).thenReturn();

    ParentSteps.saveHttpResp(response);
  }
}
