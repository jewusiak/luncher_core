package pl.luncher.v3.luncher_core.it.config;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import pl.luncher.v3.luncher_core.place.persistence.repositories.PlaceRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.ForgottenPasswordIntentRepository;
import pl.luncher.v3.luncher_core.place.persistence.repositories.PlaceTypeRepository;
import pl.luncher.v3.luncher_core.common.persistence.repositories.UserRepository;
import pl.luncher.v3.luncher_core.it.steps.ParentSteps;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@CucumberContextConfiguration
@ActiveProfiles("local_test")
//@MockBeans({
//    @MockBean(UserRepository.class)
//})
@Slf4j
@RequiredArgsConstructor
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class TestsItConfig {

  private final static boolean shouldDeleteAll = true;

  private static GenericContainer<?> elasticsearchContainer;
  private static GenericContainer<?> postgresContainer;
  private static boolean elasticsearchStarted;
  private static boolean postgresStarted;

  @LocalServerPort
  private int port;

  private final Environment environment;

  private final UserRepository userRepository;
  private final PlaceRepository placeRepository;
  private final PlaceTypeRepository placeTypeRepository;
  @Autowired
  private ForgottenPasswordIntentRepository forgottenPasswordIntentRepository;

  @BeforeAll
  public static void beforeAll() throws InterruptedException {
    log.info("BEFORE ALL >>");

    if (!isHostRunningWindows()) {
      if (postgresNotRunning()) {
        setUpPostgresContainer();
      }
      if (elasticsearchNotRunning()) {
        setUpElasticsearchContainer();
      }
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

  private static boolean postgresNotRunning() {
    try (Socket ignored = new Socket("localhost", 5432)) {
      log.warn("External postgres is running.");
      return false;
    } catch (ConnectException e) {
      log.info("External postgres is not running.");
      return true;
    } catch (IOException e) {
      throw new IllegalStateException("Error while trying to check open port", e);
    }
  }

  @AfterAll
  public static void afterAll() {

    log.info("AFTER ALL >>");
    if (elasticsearchStarted) {
      tearDownElasticsearchContainer();
    }
    if (postgresStarted) {
      tearDownPostgresContainer();
    }
    ParentSteps.resetAll();
  }

  @Before
  public void before() {
    log.info("BEFORE EACH >>");
    RestAssured.port = port;
  }

  private static void setUpElasticsearchContainer() {
    log.info("BA >> Set up Elasticsearch");
    elasticsearchContainer = new GenericContainer<>(DockerImageName.parse("luncher_core_elasticsearch"));
    elasticsearchContainer.withEnv("discovery.type", "single-node");
    elasticsearchContainer.withEnv("xpack.security.enabled", "false");
    elasticsearchContainer.withExposedPorts(9200, 9300);
    elasticsearchContainer.setPortBindings(List.of("9200:9200", "9300:9300"));
    elasticsearchContainer.start();
    elasticsearchStarted = true;
    log.info("BA >> Elasticsearch is up and running");
  }

  private static boolean isHostRunningWindows() {
    String property = System.getProperty("os.name");
    log.info("Running on {} machine.", property);
    return property != null && property.startsWith("Windows");
  }

  /**
   * Creating a Docker image should be handled externally - preferably by Docker engine (bash?)
   */
  private static void setUpPostgresContainer() {
    log.info("BA >> Set up Postgres");
    postgresContainer = new GenericContainer<>(DockerImageName.parse("luncher_core_postgres"));

    // below env vars seem to be overwritten by testcontainers/image etc.
    postgresContainer.withEnv("POSTGRES_DB", "luncher_core");
    postgresContainer.withEnv("POSTGRES_USER", "testuser");
    postgresContainer.withEnv("POSTGRES_PASSWORD", "testpass");

    postgresContainer.withCopyFileToContainer(MountableFile.forClasspathResource("psql_init/"),
        "/docker-entrypoint-initdb.d/");
    postgresContainer.withExposedPorts(5432);
    postgresContainer.setPortBindings(List.of("5432:5432"));
    postgresContainer.start();
    postgresStarted = true;
    log.info("BA >> Postgres is up and running");
  }

  private static void tearDownElasticsearchContainer() {
    log.info("AA >> Tear down Elasticsearch");
    elasticsearchContainer.stop();
    elasticsearchStarted = false;
    log.info("AA >> Elasticsearch is down");
  }

  private static void tearDownPostgresContainer() {
    log.info("AA >> Tear down Postgres");
    postgresContainer.stop();
    postgresStarted = false;
    log.info("AA >> Postgres is down");
  }

  @After
  public void after() {
    log.info("AFTER EACH >>");
    if (shouldDeleteAll) {
      placeRepository.deleteAll();
      forgottenPasswordIntentRepository.deleteAll();
      userRepository.deleteAll();
      placeTypeRepository.deleteAll();
    }
  }
}
