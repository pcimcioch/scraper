package scraper.module.core.properties.option;

import scraper.exception.IllegalAnnotationException;
import scraper.exception.ValidationException;
import scraper.module.core.properties.PropertyDescriptor;
import scraper.module.core.properties.PropertyParser;

import java.lang.annotation.Annotation;

/**
 * Parser for {@link EnumProperty}.
 */
public class EnumPropertyParser implements PropertyParser<EnumProperty> {

    @Override
    public void validate(Object value, Annotation annotation) throws ValidationException {
        if (!(annotation instanceof EnumProperty)) {
            throw new IllegalAnnotationException("Annotation must be EnumProperty annotation");
        }

        if (value != null && !isApplicable(value.getClass())) {
            throw new IllegalAnnotationException("Value must be an Enum");
        }
    }

    @Override
    public PropertyDescriptor getDescriptor(String propertyName, Class<?> fieldType, Annotation annotation) {
        if (!(annotation instanceof EnumProperty)) {
            throw new IllegalAnnotationException("Annotation must be EnumProperty annotation");
        }
        if (!isApplicable(fieldType)) {
            throw new IllegalAnnotationException("Annotation EnumProperty cannot be applied to field [%s] with type [%s]", propertyName, fieldType.getCanonicalName());
        }

        return new EnumPropertyDescriptor(propertyName, (Class<? extends Enum>) fieldType, (EnumProperty) annotation);
    }

    @Override
    public Class<EnumProperty> getAnnotationType() {
        return EnumProperty.class;
    }

    protected boolean isApplicable(Class<?> type) {
        return type.isEnum();
    }
}
