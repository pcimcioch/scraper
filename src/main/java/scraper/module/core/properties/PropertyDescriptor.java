package scraper.module.core.properties;

import com.fasterxml.jackson.annotation.JsonProperty;

import scraper.util.Utils;

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

    public String getPropertyName() {
        return propertyName;
    }

    public String getViewName() {
        return viewName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

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
