package cn.org.atool.fluent.mybatis.and;

import com.mybatisplus.core.conditions.AbstractWrapper;

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
