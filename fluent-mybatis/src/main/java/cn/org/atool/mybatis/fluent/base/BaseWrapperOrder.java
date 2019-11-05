package cn.org.atool.mybatis.fluent.base;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;

public abstract class BaseWrapperOrder<Q extends AbstractWrapper & IProperty2Column> {
    private final Q query;

    protected BaseWrapperOrder(Q query) {
        this.query = query;
    }

    public Q endOrder() {
        return this.query;
    }
}
