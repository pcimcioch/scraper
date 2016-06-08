package scraper.module.core.properties.string;

import scraper.module.core.properties.PropertyDescriptor;
import scraper.util.Utils;

public class StringPropertyDescriptor extends PropertyDescriptor {

    private static final String TYPE = "text";

    private final int minLength;

    private final int maxLength;

    private final String pattern;

    public StringPropertyDescriptor(String propertyName, StringProperty property) {
        this(propertyName, property.viewName(), property.description(), property.minLength(), property.maxLength(), property.pattern(), property.required());
    }

    public StringPropertyDescriptor(String propertyName, String viewName, String description, int minLength, int maxLength, String pattern, boolean required) {
        super(propertyName, viewName, description, required);
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.pattern = pattern;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public String type() {
        return TYPE;
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(super.hashCode(), minLength, maxLength, pattern);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        StringPropertyDescriptor other = (StringPropertyDescriptor) obj;

        return Utils.computeEq(minLength, other.minLength, maxLength, other.maxLength, pattern, other.pattern);
    }
}
