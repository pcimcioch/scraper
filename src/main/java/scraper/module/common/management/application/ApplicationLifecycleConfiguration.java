package scraper.module.common.management.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import scraper.module.common.web.CommonWebConfigurer;

/**
 * Application liecycle webpage configuration.
 * <p>
 * Registers resource handlers for lifecycle webpage, and registers as webpage in {@link CommonWebConfigurer}.
 */
@Configuration
public class ApplicationLifecycleConfiguration extends WebMvcConfigurerAdapter implements CommonWebConfigurer {

    private static final String MAIN_PAGE = ApplicationLifecycleModule.NAME + "/index.html";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + ApplicationLifecycleModule.NAME + "/**").addResourceLocations("classpath:/resources/scraper/module/common/management/application/");
    }

    @Override
    public String url() {
        return MAIN_PAGE;
    }

    @Override
    public String moduleDescription() {
        return ApplicationLifecycleModule.DESCRIPTION;
    }
}
