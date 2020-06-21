package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.interfaces.IEntity;
import cn.org.atool.fluent.mybatis.interfaces.IUpdate;
import lombok.Getter;

import java.util.Map;

/**
 * BaseSetter: 更新设置操作
 *
 * @param <U> 更新器
 * @author darui.wu
 */
public abstract class BaseSetter<E extends IEntity, U extends IUpdate<E, U, ?>> {
    @Getter
    private U updater;

    protected BaseSetter(U updater) {
        this.updater = updater;
    }

    /**
     * 按照values中非null值更新记录
     *
     * @param values
     * @return
     */
    public U eqByNotNull(Map<String, Object> values) {
        if (values != null) {
            values.forEach((column, value) -> updater.set(column, value));
        }
        return updater;
    }
}