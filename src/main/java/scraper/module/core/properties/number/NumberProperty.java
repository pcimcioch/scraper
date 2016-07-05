package scraper.module.core.properties.number;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for number property.
 * <p>
 * Applies to fields of type {@link Short}, {@link Integer}, {@link Long}, {@link Byte}, {@code short}, {@code int}, {@code long} and {@code byte}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NumberProperty {

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
     * Minimum value.
     * <p>
     * Optional. By default will be minimum value of annotated type ({@link Short#MIN_VALUE}, {@link Integer#MIN_VALUE}, {@link Long#MIN_VALUE} or {@link Byte#MIN_VALUE})
     *
     * @return minimum value
     */
    long min() default Long.MIN_VALUE;

    /**
     * Maximum value.
     * <p>
     * Optional. By default will be maximum value of annotated type ({@link Short#MAX_VALUE}, {@link Integer#MAX_VALUE}, {@link Long#MAX_VALUE} or {@link Byte#MAX_VALUE})
     *
     * @return maximum value
     */
    long max() default Long.MAX_VALUE;

    /**
     * If value is required (must be non-null).
     * <p>
     * Optional. <tt>true</tt> by default.
     *
     * @return if value is required.
     */
    boolean required() default true;
}
