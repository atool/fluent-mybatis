package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.annotation.FieldMeta;
import cn.org.atool.fluent.mybatis.interfaces.IUpdate;

import java.util.Map;

/**
 * BaseSetter: 更新设置操作
 *
 * @param <S> 更新器
 * @author darui.wu
 */
public abstract class UpdateBase<
    S extends UpdateBase<S, U>,
    U extends IUpdate<?, U, ?>
    >
    extends BaseSegment<UpdateApply<S, U>, U> {

    private final UpdateApply<S, U> apply = new UpdateApply<>((S) this);

    protected UpdateBase(U updater) {
        super(updater);
    }

    /**
     * 按照values中非null值更新记录
     *
     * @param values
     * @return
     */
    public S eqByNotNull(Map<String, Object> values) {
        if (values != null) {
            values.forEach((column, value) -> this.wrapperData().updateSet(column, value));
        }
        return (S) this;
    }


    @Override
    public UpdateApply<S, U> set(FieldMeta field) {
        return apply.setCurrentField(field);
    }
}