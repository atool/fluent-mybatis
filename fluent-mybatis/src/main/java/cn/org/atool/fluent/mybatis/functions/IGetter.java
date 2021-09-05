package cn.org.atool.fluent.mybatis.functions;

import java.io.Serializable;

/**
 * getter方法
 *
 * @author wudarui
 */
@FunctionalInterface
public interface IGetter<E> extends Serializable {
    /**
     * 返回属性值
     *
     * @param entity Entity
     * @return 属性值
     */
    Object get(E entity);
}
