package scraper.module.core.properties.string;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StringProperty {

    String viewName();

    String description();

    int minLength() default 0;

    int maxLength() default 255;

    String pattern() default "";

    boolean required() default true;
}
