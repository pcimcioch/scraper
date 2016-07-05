package scraper.module.common.management.module.view;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import scraper.module.common.web.CommonWebConfigurer;

/**
 * Modules view webpage configuration.
 * <p>
 * Registers resource handlers for modules view webpage, and registers as webpage in {@link CommonWebConfigurer}.
 */
@Configuration
public class ModuleViewConfiguration extends WebMvcConfigurerAdapter implements CommonWebConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + ModuleViewModule.NAME + "/**").addResourceLocations("classpath:/resources/scraper/module/common/management/module/view/");
    }

    @Override
    public String getModuleName() {
        return ModuleViewModule.NAME;
    }

    @Override
    public String getModuleDescription() {
        return ModuleViewModule.DESCRIPTION;
    }
}
