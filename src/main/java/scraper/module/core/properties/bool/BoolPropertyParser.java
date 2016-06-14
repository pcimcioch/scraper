package scraper.module.core.properties.bool;

import scraper.exception.IllegalAnnotationException;
import scraper.exception.ValidationException;
import scraper.module.core.properties.PropertyDescriptor;
import scraper.module.core.properties.PropertyParser;

import java.lang.annotation.Annotation;

public class BoolPropertyParser implements PropertyParser<BoolProperty> {

    @Override
    public void validate(Object value, Annotation annotation) throws ValidationException {
        if (!(annotation instanceof BoolProperty)) {
            throw new IllegalAnnotationException("Annotation must be BoolProperty annotation");
        }

        if (value != null && !isApplicable(value.getClass())) {
            throw new IllegalAnnotationException("Value must be a Boolean");
        }
    }

    @Override
    public PropertyDescriptor getDescriptor(String propertyName, Class<?> fieldType, Annotation annotation) {
        if (!(annotation instanceof BoolProperty)) {
            throw new IllegalAnnotationException("Annotation must be BoolProperty annotation");
        }
        if (!isApplicable(fieldType)) {
            throw new IllegalAnnotationException("Annotation BoolProperty cannot be applied to field %s with type %s", propertyName, fieldType.getCanonicalName());
        }

        return new BoolPropertyDescriptor(propertyName, (BoolProperty) annotation);
    }

    @Override
    public Class<BoolProperty> getAnnotationType() {
        return BoolProperty.class;
    }

    protected boolean isApplicable(Class<?> type) {
        return boolean.class.equals(type) || Boolean.class.equals(type);
    }
}
