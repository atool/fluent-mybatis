package cn.org.atool.fluent.utils;

/**
 * 简单键值对封装
 *
 * @param <K> key值类型
 * @param <V> val值类型
 * @author wudarui
 */
public class KeyVal<K, V> {
    private final K key;

    private final V val;

    public KeyVal(K key, V val) {
        this.key = key;
        this.val = val;
    }

    public K key() {
        return this.key;
    }

    public V val() {
        return this.val;
    }
}
