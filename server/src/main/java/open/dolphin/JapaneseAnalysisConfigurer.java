package open.dolphin;


import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;

/**
 * 日本語 analyzer composed with Elasticsearch analysis-kuromoji and analysis-icu.
 * persistence.xml に記載して使用する.
 */
public class JapaneseAnalysisConfigurer implements ElasticsearchAnalysisConfigurer {

    @Override
    public void configure(ElasticsearchAnalysisConfigurationContext context) {
        context.analyzer("japanese").custom()
            .tokenizer("kuromoji_tokenizer")
            .charFilters("icu_normalizer", "kuromoji_iteration_mark")
            .tokenFilters("kuromoji_baseform", "ja_stop", "kuromoji_number", "kuromoji_stemmer");
    }
}
