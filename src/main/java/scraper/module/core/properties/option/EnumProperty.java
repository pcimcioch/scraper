package scraper.module.core.properties.option;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for enum property.
 * <p>
 * Applies to fields of type {@link Enum}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnumProperty {

    /**
     * Name of the property.
     *
     * @return name of the property
     */
    String viewName();

    /**
     * Description of the property.
     *
     * @return property description
     */
    String description();

    /**
     * If value is required (must be non-null).
     * <p>
     * Optional. <tt>true</tt> by default.
     *
     * @return if value is required.
     */
    boolean required() default true;
}
