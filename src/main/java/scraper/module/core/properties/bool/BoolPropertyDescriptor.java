package scraper.module.core.properties.bool;

import scraper.module.core.properties.PropertyDescriptor;

/**
 * Descriptor of the {@link BoolProperty}.
 */
public class BoolPropertyDescriptor extends PropertyDescriptor {

    private static final String TYPE = "bool";

    public BoolPropertyDescriptor(String propertyName, BoolProperty property) {
        this(propertyName, property.viewName(), property.description());
    }

    public BoolPropertyDescriptor(String propertyName, String viewName, String description) {
        super(propertyName, viewName, description, true);
    }

    @Override
    public String type() {
        return TYPE;
    }
}
