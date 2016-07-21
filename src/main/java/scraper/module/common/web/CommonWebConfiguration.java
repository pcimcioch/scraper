package scraper.module.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

/**
 * Common web configuration.
 * <p>
 * Adds resource handlers for common web resources, used by different modules and webpage for "subpages" support.
 * <p>
 * "Subpages" is small webpage that shows links to all standalone modules webpages. Standalone module, to register it's webpage, must implement {@link CommonWebConfigurer}.
 */
@Configuration
@EnableWebSocketMessageBroker
public class CommonWebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler('/' + CommonWebModule.NAME + "/**").addResourceLocations("classpath:/resources/scraper/module/common/web/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/scraper/module/common/web/subpages/");
    }
}
