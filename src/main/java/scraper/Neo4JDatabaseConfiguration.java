package scraper;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import scraper.environment.Workspace;

/**
 * Configuration of neo4j database.
 */
@Configuration
@EnableNeo4jRepositories(basePackageClasses = Neo4JDatabaseConfiguration.class)
@EnableTransactionManagement
// TODO update neo4j version to 4.x
public class Neo4JDatabaseConfiguration extends Neo4jConfiguration {

    public Neo4JDatabaseConfiguration() {
        setBasePackage(this.getClass().getPackage().getName());
    }

    @Bean
    GraphDatabaseService graphDatabaseService(Workspace workspace, @Value("${neo4j.path}") String dbPath, @Value("${neo4j.username}") String username,
            @Value("${neo4j.password}") String password) {
        System.setProperty("username", username);
        System.setProperty("password", password);

        return new GraphDatabaseFactory().newEmbeddedDatabase(dbPath);
    }
}
