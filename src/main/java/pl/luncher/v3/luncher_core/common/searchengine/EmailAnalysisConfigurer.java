package pl.luncher.v3.luncher_core.common.searchengine;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

public class EmailAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {
    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext elasticsearchAnalysisConfigurationContext) {
        elasticsearchAnalysisConfigurationContext.analyzer("email").custom()
                .tokenizer("uax_url_email")
                .tokenFilters("email", "lowercase", "unique");
        elasticsearchAnalysisConfigurationContext.tokenFilter("email").type("pattern_capture")
                .param("preserve_original", true)
                .param("patterns", "^([^@]+)", "(\\d+)"); //, "(\\p{L}+)"
    }
}
