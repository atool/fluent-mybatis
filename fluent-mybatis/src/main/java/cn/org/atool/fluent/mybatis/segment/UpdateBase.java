package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IUpdate;

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

    public final S set = (S) this;

    protected UpdateBase(U updater) {
        super(updater);
    }

    /**
     * 按照values中非null值更新记录
     *
     * @param values
     * @return
     */
    public S byNotNull(Map<String, Object> values) {
        if (values != null) {
            values.forEach((column, value) -> this.wrapperData().updateSet(column, value));
        }
        return (S) this;
    }

    @Override
    protected UpdateApply<S, U> apply() {
        return apply;
    }
}