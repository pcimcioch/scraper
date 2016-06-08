package scraper.module.core.properties.string;

import scraper.exception.ValidationException;
import scraper.module.core.properties.PropertyDescriptor;
import scraper.module.core.properties.PropertyParser;
import scraper.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringPropertyParser implements PropertyParser<StringProperty> {

    @Override
    public void validate(Object value, Annotation annotation) {
        if (!(annotation instanceof StringProperty)) {
            throw new IllegalArgumentException("Annotation must be StringProperty annotation");
        }

        if (value != null && !isApplicable(value.getClass())) {
            throw new IllegalArgumentException("Value must be a String");
        }

        validate((String) value, (StringProperty) annotation);
    }

    private void validate(String value, StringProperty annotation) {
        int minLength = annotation.minLength();
        int maxLength = annotation.maxLength();
        String pattern = annotation.pattern();
        boolean required = annotation.required();

        if (StringUtils.isBlank(value) && !required) {
            return;
        }

        validateRequired(required, value);
        validateMinLength(minLength, value);
        validateMaxLength(maxLength, value);
        validatePattern(pattern, value);
    }

    private void validateRequired(boolean required, String value) {
        if (required && StringUtils.isBlank(value)) {
            throw new ValidationException("Value must be present");
        }
    }

    private void validateMinLength(int minLength, String value) {
        if (value.length() < minLength) {
            throw new ValidationException("Value \"%s\" must be at least %d characters long", value, minLength);
        }
    }

    private void validateMaxLength(int maxLength, String value) {
        if (value.length() > maxLength) {
            throw new ValidationException("Value \"%s\" must be maximum %d characters long", value, maxLength);
        }
    }

    private void validatePattern(String pattern, String value) {
        if (StringUtils.isBlank(pattern)) {
            return;
        }

        try {
            if (!Pattern.matches(pattern, value)) {
                throw new ValidationException("Value \"%s\" does not match pattern \"%s\"", value, pattern);
            }
        } catch (PatternSyntaxException ex) {
            throw new ValidationException("Incorrect pattern \"%s\". %s", ex, pattern, ex.getMessage());
        }
    }

    @Override
    public PropertyDescriptor getDescriptor(String propertyName, Class<?> fieldType, Annotation annotation) {
        if (!(annotation instanceof StringProperty)) {
            throw new IllegalArgumentException("Annotation must be StringProperty annotation");
        }
        if (!isApplicable(fieldType)) {
            throw new IllegalArgumentException("Annotation StringProperty cannot be applied to field " + propertyName + " with type " + fieldType.getCanonicalName());
        }

        return new StringPropertyDescriptor(propertyName, (StringProperty) annotation);
    }

    @Override
    public Class<StringProperty> getAnnotationType() {
        return StringProperty.class;
    }

    protected boolean isApplicable(Class<?> type) {
        return String.class.equals(type);
    }
}
