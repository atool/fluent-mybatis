package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;

/**
 * getter方法
 *
 * @author wudarui
 */
@FunctionalInterface
public interface IGetter {
    /**
     * 返回属性值
     *
     * @param entity Entity
     * @return 属性值
     */
    Object get(IEntity entity);
}
