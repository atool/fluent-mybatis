package cn.org.atool.fluent.mybatis.functions;

import java.io.Serializable;

/**
 * IEntity setter(value)函数
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
