package pl.luncher.v3.luncher_core.it.config;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;
import pl.luncher.v3.luncher_core.common.repositories.UserRepository;
import pl.luncher.v3.luncher_core.it.steps.ParentSteps;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
@ActiveProfiles("local_test")
//@MockBeans({
//    @MockBean(UserRepository.class)
//})
@Slf4j
@RequiredArgsConstructor
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestsItConfig {

  private static ElasticsearchContainer elasticsearchContainer;

  @LocalServerPort
  private int port;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PlaceRepository placeRepository;
  @Autowired
  private PlaceTypeRepository placeTypeRepository;

  @BeforeAll
  public static void beforeAll() throws InterruptedException {
//    log.info("BA >> Set up Elasticsearch");
//    elasticsearchContainer = new ElasticsearchContainer("elasticsearch:8.12.2");
//    elasticsearchContainer.withEnv("discovery.type", "single-node");
//    elasticsearchContainer.withEnv("xpack.security.enabled", "false");
//    elasticsearchContainer.withExposedPorts(9200, 9300);
//    elasticsearchContainer.setPortBindings(List.of("9200:9200", "9300:9300"));
//    elasticsearchContainer.start();
//    log.info("BA >> Elasticsearch is up and running");
    log.info("BEFORE ALL >>");

  }

  @AfterAll
  public static void afterAll() {
//    log.info("AA >> Tear down Elasticsearch");
//    elasticsearchContainer.stop();
//    log.info("AA >> Elasticsearch is down");
    log.info("AFTER ALL >>");
    ParentSteps.resetAll();
  }

  @Before
  public void before() {
    log.info("BEFORE EACH >>");
    RestAssured.port = port;
  }

  @After
  public void after() {
    log.info("AFTER EACH >>");
    placeRepository.deleteAll();
    userRepository.deleteAll();
  }
}
