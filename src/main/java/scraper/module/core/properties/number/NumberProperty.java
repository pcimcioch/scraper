package scraper.module.core.properties.number;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NumberProperty {

    String viewName();

    String description();

    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;

    boolean required() default true;
}
