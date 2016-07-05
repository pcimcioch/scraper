package scraper.module.core.properties.bool;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for boolean property.
 * <p>
 * Applies to fields of type {@link Boolean} and {@code boolean}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface BoolProperty {

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
}
