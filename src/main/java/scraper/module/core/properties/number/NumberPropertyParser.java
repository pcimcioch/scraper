package scraper.module.core.properties.number;

import scraper.exception.IllegalAnnotationException;
import scraper.exception.ValidationException;
import scraper.module.core.properties.PropertyDescriptor;
import scraper.module.core.properties.PropertyParser;

import java.lang.annotation.Annotation;

/**
 * Parser for {@link NumberProperty}.
 */
public class NumberPropertyParser implements PropertyParser<NumberProperty> {

    @Override
    public void validate(Object value, Annotation annotation) {
        if (!(annotation instanceof NumberProperty)) {
            throw new IllegalAnnotationException("Annotation must be NumberProperty annotation");
        }

        if (value != null && !isApplicable(value.getClass())) {
            throw new IllegalAnnotationException("Value must be a number");
        }

        validate(value == null ? null : ((Number) value).longValue(), (NumberProperty) annotation);
    }

    private void validate(Long value, NumberProperty annotation) {
        long min = annotation.min();
        long max = annotation.max();
        boolean required = annotation.required();

        if (value == null && !required) {
            return;
        }

        validateRequired(required, value);
        validateMin(min, value);
        validateMax(max, value);
    }

    private void validateRequired(boolean required, Long value) {
        if (required && value == null) {
            throw new ValidationException("Value must be present");
        }
    }

    private void validateMin(long min, Long value) {
        if (value < min) {
            throw new ValidationException("Value [%d] must be greater or equal %d", value, min);
        }
    }

    private void validateMax(long max, Long value) {
        if (value > max) {
            throw new ValidationException("Value [%s] must be lower or equal %d", value, max);
        }
    }

    @Override
    public PropertyDescriptor getDescriptor(String propertyName, Class<?> fieldType, Annotation annotation) {
        if (!(annotation instanceof NumberProperty)) {
            throw new IllegalAnnotationException("Annotation must be NumberProperty annotation");
        }
        if (!isApplicable(fieldType)) {
            throw new IllegalAnnotationException("Annotation NumberProperty cannot be applied to field [%s] with type [%s]", propertyName, fieldType.getCanonicalName());
        }

        return new NumberPropertyDescriptor(propertyName, fieldType, (NumberProperty) annotation);
    }

    @Override
    public Class<NumberProperty> getAnnotationType() {
        return NumberProperty.class;
    }

    protected boolean isApplicable(Class<?> type) {
        return Short.class.equals(type) || short.class.equals(type) || Integer.class.equals(type) || int.class.equals(type) || Long.class.equals(type) || long.class.equals(type)
                || byte.class.equals(type) || Byte.class.equals(type);
    }
}
