package scraper.module.core.properties;

import com.fasterxml.jackson.annotation.JsonProperty;
import scraper.util.Utils;

/**
 * Base for property descriptors.
 * <p>
 * Contains all basic attributes available for all properties.
 */
public abstract class PropertyDescriptor {

    private final String propertyName;

    private final String viewName;

    private final String description;

    private final boolean required;

    protected PropertyDescriptor(String propertyName, String viewName, String description, boolean required) {
        this.propertyName = propertyName;
        this.viewName = viewName;
        this.description = description;
        this.required = required;
    }

    /**
     * Gets property name.
     * <p>
     * It should be the name that is used to deserialize field from {@link ClassPropertyDescriptor} to real instance.
     *
     * @return property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Gets view name.
     * <p>
     * This is the name that is visible in metamodel.
     *
     * @return view name
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * Gets property descriptor.
     * <p>
     * This is kind of a hint/description for this property visible in metamodel.
     *
     * @return description of the property
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets if property is required, or it could be <tt>null</tt>.
     *
     * @return <tt>true</tt> if property is required, <tt>false</tt> otherwise
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Returns type of the property.
     * <p>
     * Type is string value, unique among all property descriptors. Modules that make use of metamodel use it to determine type of the property.
     *
     * @return type of the property
     */
    @JsonProperty
    public abstract String type();

    @Override
    public int hashCode() {
        return Utils.computeHash(description, propertyName, viewName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PropertyDescriptor other = (PropertyDescriptor) obj;

        return Utils.computeEq(description, other.description, propertyName, other.propertyName, viewName, other.viewName);
    }
}
