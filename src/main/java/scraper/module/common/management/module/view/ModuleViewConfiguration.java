package scraper.module.common.management.module.view;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import scraper.module.common.web.CommonWebConfigurer;

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
