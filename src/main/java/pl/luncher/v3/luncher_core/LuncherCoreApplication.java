package pl.luncher.v3.luncher_core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
@ComponentScan({"pl.luncher.common", "pl.luncher.v3.luncher_core"})
@EnableJpaRepositories({"pl.luncher.common.infrastructure.persistence"})
@EntityScan({"pl.luncher.common.infrastructure.persistence"})
public class LuncherCoreApplication {


  public static void main(String[] args) {
    SpringApplication.run(LuncherCoreApplication.class, args);
  }

}
