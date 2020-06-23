package cn.org.atool.fluent.mybatis.condition.base;

import cn.org.atool.fluent.mybatis.annotation.FieldMeta;
import cn.org.atool.fluent.mybatis.condition.model.WrapperData;
import cn.org.atool.fluent.mybatis.interfaces.IWrapper;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * BaseSegment
 *
 * @param <R>
 * @param <W>
 * @author darui.wu
 * @create 2020/6/22 10:47 上午
 */
public abstract class BaseSegment<R, W extends IWrapper<?, W, ?>> {
    @Getter(AccessLevel.PACKAGE)

    protected final BaseWrapper wrapper;

    protected BaseSegment(W wrapper) {
        this.wrapper = (BaseWrapper) wrapper;
    }

    /**
     * 对字段column进行操作
     *
     * @param field 字段信息
     * @return BaseSegment子类或者操作器
     */
    public abstract R set(FieldMeta field);

    /**
     * 结束本段操作，返回查询（更新）器对象
     *
     * @return 查询（更新）器对象
     */
    public W end() {
        return (W) this.wrapper;
    }

    WrapperData wrapperData() {
        return this.wrapper.getWrapperData();
    }
}