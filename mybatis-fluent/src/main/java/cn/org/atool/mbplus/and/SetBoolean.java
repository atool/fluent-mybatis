package cn.org.atool.mbplus.and;

import cn.org.atool.mbplus.base.IEntityUpdate;


public class SetBoolean<U extends IEntityUpdate> extends SetObject<Boolean, U> {
    public SetBoolean(U wrapper, String column, String property) {
        super(wrapper, column, property);
    }

    /**
     * 更新 #{column} = true
     *
     * @return
     */
    public U isTrue() {
        return super.is(true);
    }

    /**
     * 更新 #{column} = false
     *
     * @return
     */
    public U isFalse() {
        return super.is(false);
    }
}
