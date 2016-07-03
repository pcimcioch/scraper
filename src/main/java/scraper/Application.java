package scraper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import scraper.environment.LifeCycle;

/**
 * Main application class.
 */
@EnableAutoConfiguration
@ComponentScan
@Configuration
@EnableAspectJAutoProxy
@EnableAsync
@EnableScheduling
public class Application implements CommandLineRunner {

    @Autowired
    private LifeCycle lifeCycle;

    @Override
    public void run(String... args) throws Exception {
        lifeCycle.waitForFinish();
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext ctx = app.run(args);
        SpringApplication.exit(ctx);
    }
}
