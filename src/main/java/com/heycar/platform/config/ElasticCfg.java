package com.heycar.platform.config;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import java.io.File;
import java.io.IOException;

/**
 * <p>
 *  This class contains the configuration specific to the elastic search storage.
 * </p>
 *
 * @since   01-06-2019
 * @author  Lalitkumar Kulkarni
 * @version 1.0
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.techprimers.elastic.repository")
public class ElasticCfg {

    private static final String HTTP_ENABLED = "http.enabled";

    private static final String SHARDS_INDEX_NO = "index.number_of_shards";

    private static final String PATH_DATA = "path.data";

    private static final String PATH_LOGS = "path.logs";

    private static final String PATH_WORK = "path.work";

    private static final String PATH_HOME = "path.home";

    private static final String TRUE = "true";

    private static final String DATA = "data";

    private static final String LOGS = "logs";

    private static final String WORK = "work";

    private static final String ELASTIC = "elastic";

    @Bean
    public NodeBuilder nodeBuilder() {
        return new NodeBuilder();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws IOException {
        File tmpDir = File.createTempFile(ELASTIC, Long.toString(System.nanoTime()));
        Settings.Builder elasticsearchSettings =
                Settings.settingsBuilder()
                        .put(HTTP_ENABLED, TRUE)
                        .put(SHARDS_INDEX_NO, "1")
                        .put(PATH_DATA, new File(tmpDir, DATA).getAbsolutePath())
                        .put(PATH_LOGS, new File(tmpDir, LOGS).getAbsolutePath())
                        .put(PATH_WORK, new File(tmpDir, WORK).getAbsolutePath())
                        .put(PATH_HOME, tmpDir);



        return new ElasticsearchTemplate(nodeBuilder()
                .local(true)
                .settings(elasticsearchSettings.build())
                .node()
                .client());
    }
}
