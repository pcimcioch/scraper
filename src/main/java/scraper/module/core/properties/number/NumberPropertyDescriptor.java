package scraper.module.core.properties.number;

import scraper.module.core.properties.PropertyDescriptor;
import scraper.util.Utils;

import java.util.Map;

public class NumberPropertyDescriptor extends PropertyDescriptor {

    private static final String TYPE = "number";

    private static final Map<Class<?>, Long> MIN_VALUES =
            Utils.map(Long.class, Long.MIN_VALUE, long.class, Long.MIN_VALUE, Integer.class, (long) Integer.MIN_VALUE, int.class, (long) Integer.MIN_VALUE, Short.class,
                    (long) Short.MIN_VALUE, short.class, (long) Short.MIN_VALUE, Byte.class, (long) Byte.MIN_VALUE, byte.class, (long) Byte.MIN_VALUE);

    private static final Map<Class<?>, Long> MAX_VALUES =
            Utils.map(Long.class, Long.MAX_VALUE, long.class, Long.MAX_VALUE, Integer.class, (long) Integer.MAX_VALUE, int.class, (long) Integer.MAX_VALUE, Short.class,
                    (long) Short.MAX_VALUE, short.class, (long) Short.MAX_VALUE, Byte.class, (long) Byte.MAX_VALUE, byte.class, (long) Byte.MAX_VALUE);

    private final long min;

    private final long max;

    public NumberPropertyDescriptor(String propertyName, Class<?> propertyType, NumberProperty property) {
        this(propertyName, propertyType, property.viewName(), property.description(), property.min(), property.max(), property.required());
    }

    public NumberPropertyDescriptor(String propertyName, Class<?> propertyType, String viewName, String description, long min, long max, boolean required) {
        super(propertyName, viewName, description, required);
        this.min = maximum(MIN_VALUES.get(propertyType), min);
        this.max = minimum(MAX_VALUES.get(propertyType), max);
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(super.hashCode(), min, max);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        NumberPropertyDescriptor other = (NumberPropertyDescriptor) obj;

        return Utils.computeEq(min, other.min, max, other.max);
    }

    private static long maximum(Long threshold, long min) {
        return threshold == null || threshold < min ? min : threshold;
    }

    private static long minimum(Long threshold, long max) {
        return threshold == null || threshold > max ? max : threshold;
    }
}
