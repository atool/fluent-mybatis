package cn.org.atool.fluent.mybatis.functions;

import java.io.Serializable;

/**
 * setter方法
 *
 * @author wudarui
 */
@FunctionalInterface
public interface ISetter<E> extends Serializable {
    /**
     * 设置属性值
     *
     * @param value 值
     */
    void set(E entity, Object value);
}
