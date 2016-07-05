package scraper.module.core.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that annotated bean should be obtained through {@link ModuleScope}.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Scope(value = ModuleScope.MODULE_SCOPE_NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface ModuleScoped {

}
