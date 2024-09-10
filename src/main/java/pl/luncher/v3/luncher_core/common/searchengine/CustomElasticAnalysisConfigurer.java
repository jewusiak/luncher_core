package pl.luncher.v3.luncher_core.common.searchengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import lombok.SneakyThrows;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;


public class CustomElasticAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

  @SneakyThrows
  @Override
  public void configure(

      // https://czterytygodnie.pl/elasticsearch/obsluga-jezykow.html

      ElasticsearchAnalysisConfigurationContext context) {
    context.analyzer("email").custom()
        .tokenizer("uax_url_email")
        .tokenFilters("email", "lowercase", "unique");
    context.tokenFilter("email").type("pattern_capture")
        .param("preserve_original", true)
        .param("patterns", "^([^@]+)", "(\\d+)"); //, "(\\p{L}+)"

    context.tokenizer("edge_ngram_pl").type("edge_ngram").param("min_ngram", 3).param("max_ngram", 10)
        .param("token_chars", "letter");

    ObjectMapper objectMapper = new ObjectMapper();

    List<String> stopwords = objectMapper.readValue(Files.readString(Path.of("src/main/resources/stopwords_pl.json")),
        List.class);

    context.tokenFilter("pl_stopwords").type("stop").param("stopwords", stopwords.toArray(String[]::new));

    context.analyzer("morfologik_polish").custom()
        .tokenizer(/*"/*edge_ngram_pl"*/"whitespace"/* or standard tokenizer*/)
        .tokenFilters("lowercase", "morfologik_stem", "pl_stopwords");
  }
}
