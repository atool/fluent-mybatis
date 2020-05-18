package cn.org.atool.fluent.mybatis.and;

import cn.org.atool.fluent.mybatis.base.BaseWrapperOrder;
import com.mybatisplus.core.conditions.AbstractWrapper;

public class ColumnOrder<Q extends AbstractWrapper, O extends BaseWrapperOrder> {
    private final Q wrapper;

    private final String column;

    private final O order;

    public ColumnOrder(Q wrapper, String column, O order) {
        this.wrapper = wrapper;
        this.column = column;
        this.order = order;
    }

    public O asc() {
        wrapper.orderByAsc(column);
        return order;
    }

    public O desc() {
        wrapper.orderByDesc(column);
        return order;
    }
}
