package scraper.module.core.properties;

import scraper.exception.IllegalAnnotationException;
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

/**
 * Factory, used to build {@link ClassPropertyDescriptor} from any {@link Class} annotated with {@link PropertyDescriptor} annotations.
 */
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

    /**
     * Builds {@link ClassPropertyDescriptor} from given {@code type}.
     * <p>
     * {@code type} must be annotated with {@link PropertyDescriptor} annotations. All classes in metamodel must also provide {@code defaultObject}, which is instance of {@code
     * type}, not necessarily correct from validation point of view {@link #validate(Object)}.
     *
     * @param type          class type
     * @param defaultObject default object
     * @return class property descriptor
     */
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

    /**
     * Validates given {@code object}.
     * <p>
     * Validates all fields in {@code object} that are annotated with property descriptor annotations.
     *
     * @param object instance to validate.
     * @throws ValidationException if validation failed
     */
    public static void validate(Object object) throws ValidationException {
        for (Field field : ReflectionUtils.getAllFields(object.getClass())) {
            try {
                validate(object, field);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ValidationException("Field [%s] exception: %s", e, fieldName(field), e.getMessage());
            } catch (ValidationException e) {
                throw new ValidationException("Field [%s] validation exception: %s", e, fieldName(field), e.getMessage());
            }
        }
    }

    private static void validate(Object obj, Field field) throws IllegalAccessException {
        boolean alreadyParsed = false;
        for (Annotation annotation : field.getAnnotations()) {
            PropertyParser<?> parser = getParser(annotation.annotationType());
            if (parser != null && alreadyParsed) {
                throw new IllegalAnnotationException("Field [%s] has more than one Property Descriptor", fieldName(field));
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
                throw new IllegalAnnotationException("Field [%s] has more than one Property Descriptor", fieldName(field));
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
