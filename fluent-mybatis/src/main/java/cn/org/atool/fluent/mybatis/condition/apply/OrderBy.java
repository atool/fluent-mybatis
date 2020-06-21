package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseOrder;

/**
 * ColumnOrder: 字段排序
 * order by column asc[desc]
 *
 * @param <O> 排序对象
 * @author darui.wu
 */
public class OrderBy<O extends BaseOrder<O>> {
    /**
     * 被排序字段
     */
    private final String column;
    /**
     * 排序对象
     */
    private final O order;

    public OrderBy(O order, String column) {
        this.order = order;
        this.column = column;
    }

    /**
     * 按顺序排
     *
     * @return 排序对象
     */
    public O asc() {
        return order.asc(this.column);
    }

    /**
     * 按降序排
     *
     * @return 排序对象
     */
    public O desc() {
        return order.desc(this.column);
    }
}