package scraper.module.core.scope;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import scraper.module.core.context.ModuleContext;
import scraper.module.core.context.ModuleDetails;
import scraper.util.StringUtils;

@Aspect
public class ModuleScopeAspect {

    private final ModuleContext moduleContext;

    @Autowired
    public ModuleScopeAspect(ModuleContext moduleContext) {
        this.moduleContext = moduleContext;
    }

    @Around(value = "@within(annotation) && !@annotation(scraper.module.core.scope.InModuleScope)")
    private Object aroundClass(ProceedingJoinPoint pjp, InModuleScope annotation) throws Throwable {
        return manageScope(pjp, annotation);
    }

    @Around(value = "@annotation(annotation)")
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
        ModuleDetails moduleDetails = getModuleDetails(annotation);
        if (StringUtils.isNotBlank(moduleDetails.getModule())) {
            moduleContext.setModuleDetails(moduleDetails);
        }
    }

    private ModuleDetails getModuleDetails(InModuleScope annotation) {
        return new ModuleDetails(annotation.module(), annotation.instance());
    }

}
