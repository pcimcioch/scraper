package scraper.module.core.scope;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;
import scraper.util.StringUtils;

/**
 * Aspect that implements {@link InModuleScope} behaviour.
 * <p>
 * All methods annotated with {@link InModuleScope}, and methods of classes annotated with {@link InModuleScope} will be run in new {@link ModuleScope}. Moreover, {@link
 * ModuleDetails} bean in module scope, will be configured according to annotation configuration.
 */
@Aspect
public class ModuleScopeAspect {

    private final ModuleContext moduleContext;

    @Autowired
    public ModuleScopeAspect(ModuleContext moduleContext) {
        this.moduleContext = moduleContext;
    }

    @Around("@within(annotation) && !@annotation(scraper.module.core.scope.InModuleScope)")
    private Object aroundClass(ProceedingJoinPoint pjp, InModuleScope annotation) throws Throwable {
        return manageScope(pjp, annotation);
    }

    @Around("@annotation(annotation)")
    private Object aroundMethod(ProceedingJoinPoint pjp, InModuleScope annotation) throws Throwable {
        return manageScope(pjp, annotation);
    }

    private Object manageScope(ProceedingJoinPoint pjp, InModuleScope annotation) throws Throwable {
        ModuleScope.instance().openScope();
        try {
            initModuleContext(annotation);
            return pjp.proceed();
        } finally {
            ModuleScope.instance().closeScope();
        }
    }

    private void initModuleContext(InModuleScope annotation) {
        if (StringUtils.isNotBlank(annotation.module())) {
            moduleContext.setModuleDetails(getModuleDetails(annotation));
        }
    }

    private ModuleDetails getModuleDetails(InModuleScope annotation) {
        String instance = StringUtils.isBlank(annotation.instance()) ? null : annotation.instance();
        return new ModuleDetails(annotation.module(), instance);
    }

}
