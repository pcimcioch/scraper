package scraper.module.common.management.module.view;

import scraper.module.core.Module;
import scraper.module.core.ServiceModule;
import scraper.module.core.StandaloneModule;
import scraper.module.core.WorkerModule;
import scraper.module.core.properties.ClassPropertyDescriptor;
import scraper.util.Utils;

import java.util.HashSet;
import java.util.Set;

/**
 * Json DTO representing {@link Module}.
 * <p>
 * This DTO is only returned by a server.
 */
public class ModuleDescriptorJsonDto {

    private String name;

    private String description;

    private ModuleType type;

    private Set<String> dependencies;

    private ClassPropertyDescriptor propertyDescriptor;

    public ModuleDescriptorJsonDto(String name, String description, Set<String> dependencies, ModuleType type, ClassPropertyDescriptor propertyDescriptor) {
        setName(name);
        setDescription(description);
        setDependencies(dependencies);
        setType(type);
        setPropertyDescriptor(propertyDescriptor);
    }

    public ModuleDescriptorJsonDto(Module module) {
        setName(module.name());
        setDescription(module.description());
        setDependencies(module.dependencies());
        setType(module);
        setPropertyDescriptor(module);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModuleType getType() {
        return type;
    }

    public void setType(ModuleType type) {
        this.type = type;
    }

    public Set<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(Set<String> dependencies) {
        this.dependencies = new HashSet<>(dependencies);
    }

    public ClassPropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public void setPropertyDescriptor(ClassPropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
    }

    private void setType(Module module) {
        if (module instanceof WorkerModule<?>) {
            setType(ModuleType.WORKER);
        } else if (module instanceof ServiceModule) {
            setType(ModuleType.SERVICE);
        } else if (module instanceof StandaloneModule) {
            setType(ModuleType.STANDALONE);
        }
    }

    private void setPropertyDescriptor(Module module) {
        if (module instanceof WorkerModule<?>) {
            ClassPropertyDescriptor descriptor = ((WorkerModule<?>) module).getSettingsClassPropertyDescriptor();
            setPropertyDescriptor(descriptor);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ModuleDescriptorJsonDto other = (ModuleDescriptorJsonDto) obj;

        return Utils.computeEq(name, other.name, description, other.description, dependencies, other.dependencies, type, other.type, propertyDescriptor, other.propertyDescriptor);

    }

    @Override
    public int hashCode() {
        return Utils.computeHash(name, description, dependencies, type, propertyDescriptor);
    }
}
