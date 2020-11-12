package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
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
    protected FieldMapping current;

    /**
     * 加上表别名的字段名称
     *
     * @return
     */
    protected String currentWithAlias() {
        return this.columnWithAlias(this.current);
    }

    /**
     * 加上表别名的字段名称
     *
     * @param column
     * @return
     */
    protected String columnWithAlias(FieldMapping column) {
        return column.alias(this.wrapper.getAlias());
    }

    /**
     * 判断是否是字段，如果是加上别名
     *
     * @param column
     * @return
     */
    protected String columnWithAlias(String column) {
        if (FieldMapping.isColumnName(column)) {
            return FieldMapping.alias(this.wrapper.getAlias(), column);
        } else {
            return column;
        }
    }

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
        this.current = field;
        return this.apply();
    }

    /**
     * 当前字段
     *
     * @return
     */
    public FieldMapping get() {
        return this.current;
    }

    protected abstract R apply();

    /**
     * 结束本段操作，返回查询（更新）器对象
     *
     * @return 查询（更新）器对象
     */
    public W end() {
        return (W) this.wrapper;
    }

    /**
     * 查询条件
     *
     * @return
     */
    WrapperData wrapperData() {
        return this.wrapper.getWrapperData();
    }
}