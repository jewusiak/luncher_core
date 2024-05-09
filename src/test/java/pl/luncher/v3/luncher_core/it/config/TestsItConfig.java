package pl.luncher.v3.luncher_core.it.config;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.spring.CucumberContextConfiguration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

@SpringBootTest
@CucumberContextConfiguration
@ActiveProfiles("local_test")
@Slf4j
public class TestsItConfig {

  private static ElasticsearchContainer elasticsearchContainer;

  @BeforeAll
  public static void beforeAll() throws InterruptedException {
    log.info("BA >> Set up Elasticsearch");
    elasticsearchContainer = new ElasticsearchContainer("elasticsearch:8.12.2");
    elasticsearchContainer.withEnv("discovery.type", "single-node");
    elasticsearchContainer.withEnv("xpack.security.enabled", "false");
    elasticsearchContainer.withExposedPorts(9200, 9300);
    elasticsearchContainer.setPortBindings(List.of("9200:9200", "9300:9300"));
    elasticsearchContainer.start();
    log.info("BA >> Elasticsearch is up and running");
  }

  @AfterAll
  public static void afterAll() {
    log.info("AA >> Tear down Elasticsearch");
    elasticsearchContainer.stop();
    log.info("AA >> Elasticsearch is down");
  }

  @Before
  public void before() {
    log.info("Do something before each scenario");
  }

  @After
  public void after() {
    log.info("Do something after each scenario");
  }
}
