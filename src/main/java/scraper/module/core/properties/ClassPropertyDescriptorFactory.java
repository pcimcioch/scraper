package scraper.module.core.properties;

import scraper.exception.ValidationException;
import scraper.module.core.properties.bool.BoolPropertyParser;
import scraper.module.core.properties.number.NumberPropertyParser;
import scraper.module.core.properties.option.EnumPropertyParser;
import scraper.module.core.properties.string.StringPropertyParser;
import scraper.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class ClassPropertyDescriptorFactory {

    private static final Map<Class<? extends Annotation>, PropertyParser<?>> parsers = new HashMap<>();

    static {
        registerParser(new StringPropertyParser());
        registerParser(new BoolPropertyParser());
        registerParser(new NumberPropertyParser());
        registerParser(new EnumPropertyParser());
    }

    private ClassPropertyDescriptorFactory() {

    }

    private static <T extends Annotation> void registerParser(PropertyParser<T> parser) {
        parsers.put(parser.getAnnotationType(), parser);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Annotation> PropertyParser<T> getParser(Class<T> annotationType) {
        return (PropertyParser<T>) parsers.get(annotationType);
    }

    public static <T> ClassPropertyDescriptor buildClassPropertyDescriptor(Class<T> type, T defaultObject) {
        ClassPropertyDescriptor classDescriptor = new ClassPropertyDescriptor(defaultObject);
        for (Field field : ReflectionUtils.getAllFields(type)) {
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(field);
            if (propertyDescriptor != null) {
                classDescriptor.addDescriptor(propertyDescriptor);
            }
        }

        return classDescriptor;
    }

    public static void validate(Object obj) throws ValidationException {
        for (Field field : ReflectionUtils.getAllFields(obj.getClass())) {
            try {
                validate(obj, field);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ValidationException("Field %s exception: %s", e, fieldName(field), e.getMessage());
            } catch (ValidationException e) {
                throw new ValidationException("Field %s validation exception: %s", e, fieldName(field), e.getMessage());
            }
        }
    }

    private static void validate(Object obj, Field field) throws IllegalAccessException {
        boolean alreadyParsed = false;
        for (Annotation annotation : field.getAnnotations()) {
            PropertyParser<?> parser = getParser(annotation.annotationType());
            if (parser != null && alreadyParsed) {
                throw new IllegalArgumentException("Field " + fieldName(field) + " has more than one Property Descriptor");
            } else if (parser != null) {
                alreadyParsed = true;
                field.setAccessible(true);
                parser.validate(field.get(obj), annotation);
            }
        }
    }

    private static PropertyDescriptor getPropertyDescriptor(Field field) {
        PropertyDescriptor propertyDescriptor = null;
        for (Annotation annotation : field.getAnnotations()) {
            PropertyParser<?> parser = getParser(annotation.annotationType());
            if (parser != null && propertyDescriptor != null) {
                throw new IllegalArgumentException("Field " + fieldName(field) + " has more than one Property Descriptor");
            } else if (parser != null) {
                propertyDescriptor = parser.getDescriptor(field.getName(), field.getType(), annotation);
            }
        }

        return propertyDescriptor;
    }

    private static String fieldName(Field field) {
        return String.format("%s.%s", field.getDeclaringClass().getName(), field.getName());
    }
}
