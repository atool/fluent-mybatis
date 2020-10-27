package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;

/**
 * 表join条件构造
 *
 * @author wudarui
 */
public interface IJoin {
    /**
     * on left = right
     *
     * @param left
     * @param right
     * @return
     */
    IJoin on(BaseWhere left, BaseWhere right);

    /**
     * on left = right
     *
     * @param left
     * @param right
     * @return
     */
    IJoin on(String left, String right);
}