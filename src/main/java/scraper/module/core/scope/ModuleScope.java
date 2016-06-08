package scraper.module.core.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public final class ModuleScope implements Scope {

    public static final String MODULE_SCOPE_NAME = "module";

    private static final ModuleScope instance = new ModuleScope();

    private ThreadLocal<Deque<ScopeAttributes>> attributesHeap;

    public static ModuleScope instance() {
        return instance;
    }

    private ModuleScope() {
        init();
    }

    protected void init() {
        attributesHeap = new ThreadLocal<Deque<ScopeAttributes>>() {
            @Override
            protected Deque<ScopeAttributes> initialValue() {
                return new LinkedList<>();
            }
        };
    }

    public void openScope() {
        Deque<ScopeAttributes> heap = getAttributesHeap();
        heap.push(new ScopeAttributes());
    }

    public void closeScope() {
        ScopeAttributes scopeAttributes = getScopeAttributes();
        Deque<ScopeAttributes> heap = getAttributesHeap();
        heap.pop();

        scopeAttributes.close();
    }

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        ScopeAttributes scopeAttributes = getScopeAttributes();
        return scopeAttributes.get(name, objectFactory);
    }

    @Override
    public Object remove(String name) {
        ScopeAttributes scopeAttributes = getScopeAttributes();
        return scopeAttributes.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        ScopeAttributes scopeAttributes = getScopeAttributes();
        scopeAttributes.registerDestructionCallback(name, callback);
    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public String getConversationId() {
        Deque<ScopeAttributes> heap = getAttributesHeap();
        return String.format("%s %d", Thread.currentThread().getName(), heap.size());
    }

    private ScopeAttributes getScopeAttributes() {
        Deque<ScopeAttributes> heap = getAttributesHeap();
        if (heap.isEmpty()) {
            throw new IllegalStateException("Accessing closed scope");
        }

        return heap.getFirst();
    }

    private Deque<ScopeAttributes> getAttributesHeap() {
        return attributesHeap.get();
    }

    private static class ScopeAttributes {

        private final Map<String, Object> objectMap = new HashMap<>();

        private final Map<String, Runnable> requestDestructionCallbacks = new LinkedHashMap<>();

        public Object get(String name, ObjectFactory<?> objectFactory) {
            if (!objectMap.containsKey(name)) {
                Object newObject = objectFactory.getObject();
                objectMap.put(name, newObject);
                return newObject;
            }

            return objectMap.get(name);
        }

        public Object remove(String name) {
            requestDestructionCallbacks.remove(name);
            return objectMap.remove(name);
        }

        public void registerDestructionCallback(String name, Runnable callback) {
            requestDestructionCallbacks.put(name, callback);
        }

        private void close() {
            objectMap.clear();
            for (Runnable runnable : requestDestructionCallbacks.values()) {
                runnable.run();
            }

            requestDestructionCallbacks.clear();
        }
    }
}
