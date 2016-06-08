package scraper.module.common.logger.view;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import scraper.module.common.web.CommonWebConfigurer;

@Configuration
public class LoggerViewConfiguration extends WebMvcConfigurerAdapter implements CommonWebConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/" + LoggerViewModule.NAME + "/**").addResourceLocations("classpath:/resources/scraper/module/common/logger/view/");
    }

    @Override
    public String getModuleName() {
        return LoggerViewModule.NAME;
    }

    @Override
    public String getModuleDescription() {
        return LoggerViewModule.DESCRIPTION;
    }
}
