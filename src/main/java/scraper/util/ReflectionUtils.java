package scraper.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reflection operations.
 */
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    /**
     * Returns all field in given class.
     *
     * @param type type to scan for fields
     * @return list of fields. Will contain all declared fields of the given class and all its superclass hierarchy. Will not contain synthetic fields
     */
    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> typeIter = type; typeIter != null; typeIter = typeIter.getSuperclass()) {
            for (Field field : typeIter.getDeclaredFields()) {
                if (!field.isSynthetic()) {
                    fields.add(field);
                }
            }
        }

        return fields;
    }
}
