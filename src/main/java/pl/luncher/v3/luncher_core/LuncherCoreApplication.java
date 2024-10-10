package pl.luncher.v3.luncher_core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
public class LuncherCoreApplication {


  public static void main(String[] args) {
    SpringApplication.run(LuncherCoreApplication.class, args);
  }

}
