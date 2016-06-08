package scraper.module.common.management.application;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import scraper.module.common.web.CommonWebConfigurer;

@Configuration
public class ApplicationLifecycleConfiguration extends WebMvcConfigurerAdapter implements CommonWebConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + ApplicationLifecycleModule.NAME + "/**").addResourceLocations("classpath:/resources/scraper/module/common/management/application/");
    }

    @Override
    public String getModuleName() {
        return ApplicationLifecycleModule.NAME;
    }

    @Override
    public String getModuleDescription() {
        return ApplicationLifecycleModule.DESCRIPTION;
    }
}
