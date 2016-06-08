package scraper.module.core.properties;

import scraper.exception.ValidationException;

import java.lang.annotation.Annotation;

public interface PropertyParser<T extends Annotation> {

    void validate(Object value, Annotation annotation) throws ValidationException;

    PropertyDescriptor getDescriptor(String propertyName, Class<?> fieldType, Annotation annotation);

    Class<T> getAnnotationType();
}
