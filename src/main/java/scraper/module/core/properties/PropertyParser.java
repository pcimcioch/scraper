package scraper.module.core.properties;

import scraper.exception.ValidationException;

import java.lang.annotation.Annotation;

/**
 * Validator that checks correctness of property annotated with any property annotation.
 *
 * @param <T> type of annotation
 */
public interface PropertyParser<T extends Annotation> {

    /**
     * Validates property value.
     *
     * @param value      value to validate
     * @param annotation annotation
     * @throws ValidationException if validation failed
     */
    void validate(Object value, Annotation annotation) throws ValidationException;

    /**
     * Gets {@link PropertyDescriptor} for given {@code fieldType} annotated with {@code annotation}.
     *
     * @param propertyName property name
     * @param fieldType    field type class
     * @param annotation   annotation on the field
     * @return property descriptor
     */
    PropertyDescriptor getDescriptor(String propertyName, Class<?> fieldType, Annotation annotation);

    /**
     * Returns class type of annotation supported by this parser.
     *
     * @return class type of supported annotation
     */
    Class<T> getAnnotationType();
}
