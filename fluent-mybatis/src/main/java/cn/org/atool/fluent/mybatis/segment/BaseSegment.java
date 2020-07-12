package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.mybatis.base.IWrapper;
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
    /**
     * 当前查询（更新）器
     */
    @Getter(AccessLevel.PACKAGE)
    protected final BaseWrapper wrapper;
    /**
     * 当前处理字段
     */
    protected FieldMapping currField;

    protected BaseSegment(W wrapper) {
        this.wrapper = (BaseWrapper) wrapper;
    }

    /**
     * 对字段column进行操作
     *
     * @param field 字段信息
     * @return BaseSegment子类或者操作器
     */
    public R set(FieldMapping field) {
        this.currField = field;
        return this.process(this.currField);
    }

    protected abstract R process(FieldMapping currField);

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