package cn.org.atool.fluent.mybatis.and;

import cn.org.atool.fluent.mybatis.condition.base.AbstractWrapper;

public class AndBoolean<Q extends AbstractWrapper> extends AndObject<Boolean, Q> {
    public AndBoolean(Q wrapper, String column, String property) {
        super(wrapper, column, property);
    }

    public Q isTrue() {
        return super.eq(true);
    }

    public Q isFalse() {
        return super.eq(false);
    }
}