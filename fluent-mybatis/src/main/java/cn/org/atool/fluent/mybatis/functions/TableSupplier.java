package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.IEntity;

/**
 * 动态获取目标表
 *
 * @author darui.wu
 */
public interface TableSupplier {
    /**
     * 根据entity获取目标表
     *
     * @param entity entity
     * @return 目标表
     */
    String get(IEntity entity);
}