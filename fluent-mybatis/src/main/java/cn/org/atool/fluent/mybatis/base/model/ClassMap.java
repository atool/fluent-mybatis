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
@SuppressWarnings("rawtypes")
public class ClassMap<T> {
    private Map<String, T> map;

    public ClassMap() {
        this.map = new HashMap<>();
    }

    public T get(Class klass) {
        return this.map.get(klass.getName());
    }

    public T get(String klass) {
        return this.map.get(klass);
    }

    public ClassMap<T> put(Class klass, T value) {
        this.map.put(klass.getName(), value);
        return this;
    }

    public boolean containsKey(Class klass) {
        return this.map.containsKey(klass.getName());
    }

    public boolean containsKey(String klass) {
        return this.map.containsKey(klass);
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

    public Set<Map.Entry<String, T>> entrySet() {
        return this.map.entrySet();
    }
}