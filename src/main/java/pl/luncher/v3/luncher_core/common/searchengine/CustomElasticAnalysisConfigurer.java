package pl.luncher.v3.luncher_core.common.searchengine;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
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

    List<String> stopwords = List.of();
    try (InputStream inp = getClass().getResourceAsStream("/stopwords_pl.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inp))) {
      stopwords = objectMapper.readValue(
          String.valueOf(reader.lines().collect(Collectors.joining(System.lineSeparator()))),
          List.class);
    }
    context.tokenFilter("pl_stopwords").type("stop").param("stopwords", stopwords.toArray(String[]::new));

    context.analyzer("morfologik_polish").custom()
        .tokenizer(/*"/*edge_ngram_pl"*/"whitespace"/* or standard tokenizer*/)
        .tokenFilters("lowercase", "morfologik_stem", "pl_stopwords");
  }
}
