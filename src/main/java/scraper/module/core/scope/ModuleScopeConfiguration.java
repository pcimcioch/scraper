package scraper.module.core.scope;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import scraper.module.core.context.ModuleContext;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ModuleScopeConfiguration {

    @Bean
    static CustomScopeConfigurer customScope() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        Map<String, Object> moduleScope = new HashMap<>();
        moduleScope.put(ModuleScope.MODULE_SCOPE_NAME, ModuleScope.instance());
        configurer.setScopes(moduleScope);

        return configurer;
    }

    @Bean
    ModuleScopeAspect moduleScopeAspect(ModuleContext moduleContext) {
        return new ModuleScopeAspect(moduleContext);
    }
}
