package cn.org.atool.fluent.mybatis.condition.base;

public abstract class BaseWrapperOrder<Q extends AbstractWrapper> {
    private final Q query;

    protected BaseWrapperOrder(Q query) {
        this.query = query;
    }

    public Q endOrder() {
        return this.query;
    }
}