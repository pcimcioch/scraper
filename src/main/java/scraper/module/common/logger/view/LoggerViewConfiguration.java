package scraper.module.common.logger.view;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import scraper.module.common.web.CommonWebConfigurer;

/**
 * Web configuration for logger view module.
 * <p>
 * Registers resource handlers for logger view webpage, and registers as webpage in {@link CommonWebConfigurer}.
 */
@Configuration
public class LoggerViewConfiguration extends WebMvcConfigurerAdapter implements CommonWebConfigurer {

    private static final String MAIN_PAGE = LoggerViewModule.NAME + "/index.html";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + LoggerViewModule.NAME + "/**").addResourceLocations("classpath:/resources/scraper/module/common/logger/view/");
    }

    @Override
    public String url() {
        return MAIN_PAGE;
    }

    @Override
    public String moduleDescription() {
        return LoggerViewModule.DESCRIPTION;
    }
}
