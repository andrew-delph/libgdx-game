package render;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RenderManager{

    private Map<Class<?>, Consumer<?>> renderConsumerMap = new HashMap<>();

    public <T> Consumer<T> put(Class<T> key, Consumer<T> c) {
        return (Consumer<T>) renderConsumerMap.put(key, c);
    }

    public <T> Consumer<T> get(Class<T> key) {
        return (Consumer<T>) renderConsumerMap.get(key);
    }
}
