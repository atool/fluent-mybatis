package cn.org.atool.fluent.mybatis.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 值对结构
 *
 * @param <K>
 * @param <V>
 */
@Data
@Accessors(chain = true)
public class Pair<K, V> {
    private K key;

    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
