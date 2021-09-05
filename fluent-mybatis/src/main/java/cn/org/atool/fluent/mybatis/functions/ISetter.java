package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;

import java.io.Serializable;

/**
 * setter方法
 *
 * @author wudarui
 */
@FunctionalInterface
public interface ISetter extends Serializable {
    /**
     * 设置属性值
     *
     * @param value 值
     */
    void set(IEntity entity, Object value);
}
