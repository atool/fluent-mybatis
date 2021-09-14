package cn.org.atool.fluent.mybatis.functions;

import cn.org.atool.fluent.mybatis.base.entity.IMapping;

/**
 * SqlSupplier
 *
 * @author wudarui
 */
@FunctionalInterface
public interface SqlSupplier {
    /**
     * 根据mapping产生对应的sql片段
     *
     * @param mapping IMapping
     * @return sql片段
     */
    String sql(IMapping mapping);
}
