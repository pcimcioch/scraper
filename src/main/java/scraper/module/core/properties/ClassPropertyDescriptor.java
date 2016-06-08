package scraper.module.core.properties;

import scraper.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public boolean addDescriptor(PropertyDescriptor descriptor) {
        return descriptors.add(descriptor);
    }

    public List<PropertyDescriptor> getDescriptors() {
        return Collections.unmodifiableList(descriptors);
    }

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
