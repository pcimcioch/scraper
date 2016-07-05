package scraper.module.core.properties;

import scraper.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Property descriptor for the class.
 * <p>
 * This class describes required properties of the class and their constraints. It also keeps default instance of the described class.
 */
public class ClassPropertyDescriptor {

    private final List<PropertyDescriptor> descriptors;

    private final Object defaultObject;

    public ClassPropertyDescriptor(Object defaultObject) {
        this(Collections.emptyList(), defaultObject);
    }

    public ClassPropertyDescriptor(List<PropertyDescriptor> descriptors, Object defaultObject) {
        this.descriptors = new ArrayList<>(descriptors);
        this.defaultObject = defaultObject;
    }

    /**
     * Adds property descriptor.
     *
     * @param descriptor property descriptor to add
     */
    public void addDescriptor(PropertyDescriptor descriptor) {
        descriptors.add(descriptor);
    }

    /**
     * Returns list of property descriptors for this class descriptor.
     *
     * @return list of property descriptors
     */
    public List<PropertyDescriptor> getDescriptors() {
        return Collections.unmodifiableList(descriptors);
    }

    /**
     * Returns default instance of described class.
     *
     * @return default instance of described class
     */
    public Object getDefaultObject() {
        return defaultObject;
    }

    @Override
    public int hashCode() {
        return Utils.computeHash(descriptors, defaultObject);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ClassPropertyDescriptor other = (ClassPropertyDescriptor) obj;

        return Utils.computeEq(descriptors, other.descriptors, defaultObject, other.defaultObject);
    }
}
