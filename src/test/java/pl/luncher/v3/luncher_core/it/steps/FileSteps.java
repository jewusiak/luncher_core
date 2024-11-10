package pl.luncher.v3.luncher_core.it.steps;

import io.cucumber.java.en.And;
import java.io.File;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.springframework.core.io.ClassPathResource;

public class FileSteps {

  @And("Response binary contents are equal to file {}")
  public void responseBinaryContentsAreEqualToFile(String path) throws IOException {
    byte[] actualContents = ParentSteps.getCachedHttpResp().getBody().asByteArray();
    byte[] expectedContents = new ClassPathResource(path).getContentAsByteArray();

    Assertions.assertThat(actualContents).isEqualTo(expectedContents);
  }

  @And("File {} exists")
  public void fileExists(String path) {
    path = ParentSteps.replaceIds(path);
    Assertions.assertThat(new File(path)).exists();
  }

  @And("File {} does not exist")
  public void fileNotExists(String path) {
    path = ParentSteps.replaceIds(path);
    Assertions.assertThat(new File(path)).doesNotExist();
  }
}
