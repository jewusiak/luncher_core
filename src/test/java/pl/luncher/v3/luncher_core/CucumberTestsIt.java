package pl.luncher.v3.luncher_core;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/tests_it",
        plugin = {"pretty", "html:target/cucumber-reports"},
        glue = {"pl.luncher.v3.luncher_core.it.steps", "pl.luncher.v3.luncher_core.it.config"})
public class CucumberTestsIt {
}
