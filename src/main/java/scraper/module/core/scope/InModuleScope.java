package scraper.module.core.scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interceptor annotation.
 * <p>
 * Indicates that annotated method, or all methods in annotated class, should be run in {@link ModuleScope}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface InModuleScope {

    /**
     * Module name.
     * <p>
     * If not provided, {@link scraper.module.core.context.ModuleDetails} should be configured programmatically.
     *
     * @return module name
     */
    String module() default "";

    /**
     * Module instance name.
     * <p>
     * Optional.
     *
     * @return module instance name
     */
    String instance() default "";
}
