package cn.org.atool.fluent.mybatis.base.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * class名称为key值的HashMap对象
 *
 * @param <T>
 * @author wudarui
 */
public class ClassMap<T> {
    private Map<String, T> map;

    public ClassMap(int initialCapacity) {
        this.map = new HashMap<>(initialCapacity);
    }

    public ClassMap() {
        this.map = new HashMap<>();
    }

    public T get(Class klass) {
        return this.map.get(klass.getName());
    }

    public ClassMap<T> put(Class klass, T value) {
        this.map.put(klass.getName(), value);
        return this;
    }

    public boolean containsKey(Class klass) {
        return this.map.containsKey(klass.getName());
    }

    /**
     * 设置成不可更改的Map实例
     *
     * @return ignore
     */
    public ClassMap<T> unmodified() {
        this.map = Collections.unmodifiableMap(this.map);
        return this;
    }

    public Set<String> keySet() {
        return this.map.keySet();
    }
}
