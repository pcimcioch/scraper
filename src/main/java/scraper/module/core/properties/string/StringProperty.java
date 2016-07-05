package scraper.module.core.properties.string;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for string property.
 * <p>
 * Applies to fields of type {@link String}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StringProperty {

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
     * Minimum length of the property value.
     * <p>
     * Optional. Default is <tt>0</tt>.
     *
     * @return minimum length of the pattern.
     */
    int minLength() default 0;

    /**
     * Maximum length of the property value.
     * <p>
     * Optional. Default is <tt>255</tt>.
     *
     * @return maximum length of the pattern.
     */
    int maxLength() default 255;

    /**
     * Pattern of the property.
     * <p>
     * Optional. If empty (<tt>""</tt> or <tt>null</tt>), then no pattern validation is required. By default no pattern is used.
     *
     * @return pattern.
     */
    String pattern() default "";

    /**
     * If value is required (must be non-null).
     * <p>
     * Optional. <tt>true</tt> by default.
     *
     * @return if value is required.
     */
    boolean required() default true;
}
