package scraper.module.common.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class CommonWebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + CommonWebModule.NAME + "/**").addResourceLocations("classpath:/resources/scraper/module/common/web/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/resources/scraper/module/common/web/subpages/");
    }
}
