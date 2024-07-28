package pl.luncher.v3.luncher_core.it.config;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.PlaceTypeRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.UserRepository;
import pl.luncher.v3.luncher_core.it.steps.ParentSteps;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;

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
  private static boolean elasticsearchstarted;

  @LocalServerPort
  private int port;

  private final Environment environment;

  private final UserRepository userRepository;
  private final PlaceRepository placeRepository;
  private final PlaceTypeRepository placeTypeRepository;

  @BeforeAll
  public static void beforeAll() throws InterruptedException {
    log.info("BEFORE ALL >>");

    if (!isHostRunningWindows() && elasticsearchNotRunning()) {
      setUpElasticsearchContainer();
    }
  }

  private static boolean elasticsearchNotRunning() {
    try (Socket ignored = new Socket("localhost", 9200)) {
      log.warn("External Elasticsearch is running.");
      return false;
    } catch (ConnectException e) {
      log.info("External Elasticsearch is not running.");
      return true;
    } catch (IOException e) {
      throw new IllegalStateException("Error while trying to check open port", e);
    }
  }

  @AfterAll
  public static void afterAll() {

    log.info("AFTER ALL >>");
    if (elasticsearchstarted) {
      tearDownElasticsearchContainer();
    }
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
    placeTypeRepository.deleteAll();
  }

  private static boolean isHostRunningWindows() {
    String property = System.getProperty("os.name");
    log.info("Running on {} machine.", property);
    return property != null && property.startsWith("Windows");
  }

  private static void setUpElasticsearchContainer() {
    log.info("BA >> Set up Elasticsearch");
    elasticsearchContainer = new ElasticsearchContainer("elasticsearch:8.12.2");
    elasticsearchContainer.withEnv("discovery.type", "single-node");
    elasticsearchContainer.withEnv("xpack.security.enabled", "false");
    elasticsearchContainer.withExposedPorts(9200, 9300);
    elasticsearchContainer.setPortBindings(List.of("9200:9200", "9300:9300"));
    elasticsearchContainer.start();
    elasticsearchstarted = true;
    log.info("BA >> Elasticsearch is up and running");
  }

  private static void tearDownElasticsearchContainer() {
    log.info("AA >> Tear down Elasticsearch");
    elasticsearchContainer.stop();
    elasticsearchstarted = false;
    log.info("AA >> Elasticsearch is down");
  }
}
