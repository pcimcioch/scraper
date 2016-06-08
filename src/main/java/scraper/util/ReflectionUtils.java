package scraper.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

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
