package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;

/**
 * setter方法
 *
 * @author wudarui
 */
@FunctionalInterface
public interface ISetter {
    /**
     * 设置属性值
     *
     * @param value 值
     */
    void set(IEntity entity, Object value);
}
