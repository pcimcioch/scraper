package scraper;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration of neo4j database.
 */
@Configuration
@EnableNeo4jRepositories
@EnableTransactionManagement
public class Neo4JDatabaseConfiguration {

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration(@Value("${neo4j.path}") String dbPath, @Value("${neo4j.username}") String username,
            @Value("${neo4j.password}") String password) {
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();

        config.driverConfiguration().setDriverClassName("org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver").setCredentials(username, password).setURI("file:///" + dbPath);

        return config;
    }

    @Bean
    public SessionFactory getSessionFactory(org.neo4j.ogm.config.Configuration configuration) {
        return new SessionFactory(configuration);
    }
}
