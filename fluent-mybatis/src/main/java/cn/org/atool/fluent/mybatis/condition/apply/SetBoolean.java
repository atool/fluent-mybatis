package cn.org.atool.fluent.mybatis.condition.apply;

import cn.org.atool.fluent.mybatis.condition.base.BaseSetter;
import cn.org.atool.fluent.mybatis.interfaces.IUpdate;

/**
 * SetBoolean: 更新布尔字段值
 *
 * @param <U> 更新器
 * @author darui.wu
 */
public class SetBoolean<U extends IUpdate> extends SetObject<Boolean, U> {
    public SetBoolean(BaseSetter setter, String column, String property) {
        super(setter, column, property);
    }

    /**
     * 更新 #{column} = true
     *
     * @return 更新器
     */
    public U isTrue() {
        return super.is(true);
    }

    /**
     * 更新 #{column} = false
     *
     * @return 更新器
     */
    public U isFalse() {
        return super.is(false);
    }
}