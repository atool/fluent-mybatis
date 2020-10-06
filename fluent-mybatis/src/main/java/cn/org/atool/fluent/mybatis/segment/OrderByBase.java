package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IWrapper;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.utility.Predicates;

import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.base.model.SqlOp.RETAIN;
import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.ORDER_BY;
import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.*;
import static cn.org.atool.fluent.mybatis.utility.Predicates.notBlank;
import static cn.org.atool.fluent.mybatis.utility.Predicates.notEmpty;

/**
 * BaseOrder: 排序对象基类
 *
 * @param <O> 排序对象
 * @author darui.wu
 */
public abstract class OrderByBase<
    O extends OrderByBase<O, W>,
    W extends IWrapper<?, W, ?>
    >
    extends BaseSegment<OrderByApply<O, W>, W> {
    /**
     * 排序字段
     */
    private final OrderByApply<O, W> apply = new OrderByApply<>((O) this);

    protected OrderByBase(W wrapper) {
        super(wrapper);
    }

    /**
     * 按升序排：ORDER BY 字段, ... ASC
     * <p>例: asc("id", "name")</p>
     *
     * @param columns 排序字段列表
     * @return 排序对象
     */
    public O asc(String... columns) {
        if (Predicates.notEmpty(columns)) {
            Stream.of(columns).forEach(column -> this.applyField(column, true));
        }
        return (O) this;
    }

    /**
     * 按升序排：ORDER BY 字段, ... ASC
     * <p>例: asc("id", "name")</p>
     *
     * @param condition 成立条件
     * @param columns   排序字段列表
     * @return 排序对象
     */
    public O asc(boolean condition, String... columns) {
        if (Predicates.notEmpty(columns) && condition) {
            Stream.of(columns).forEach(column -> this.applyField(column, true));
        }
        return (O) this;
    }

    /**
     * 按升序排：ORDER BY 字段, ... ASC
     * <p>例: asc("id", "name")</p>
     *
     * @param columns 排序字段列表
     * @return 排序对象
     */
    public O asc(FieldMapping... columns) {
        if (Predicates.notEmpty(columns)) {
            Stream.of(columns).forEach(field -> this.applyField(field, true));
        }
        return (O) this;
    }

    /**
     * 按降序排：ORDER BY 字段, ... DESC
     * <p>例: desc("id", "name")</p>
     *
     * @param columns 排序字段列表
     * @return 排序对象
     */
    public O desc(String... columns) {
        if (Predicates.notEmpty(columns)) {
            Stream.of(columns).forEach(column -> this.applyField(column, false));
        }
        return (O) this;
    }

    /**
     * 按降序排：ORDER BY 字段, ... DESC
     * <p>例: desc("id", "name")</p>
     *
     * @param condition 成立条件
     * @param columns   排序字段列表
     * @return 排序对象
     */
    public O desc(boolean condition, String... columns) {
        if (Predicates.notEmpty(columns) && condition) {
            Stream.of(columns).forEach(column -> this.applyField(column, false));
        }
        return (O) this;
    }

    /**
     * 按降序排：ORDER BY 字段, ... DESC
     * <p>例: desc("id", "name")</p>
     *
     * @param columns 排序字段列表
     * @return 排序对象
     */
    public O desc(FieldMapping... columns) {
        if (Predicates.notEmpty(columns)) {
            Stream.of(columns).forEach(field -> this.applyField(field, false));
        }
        return (O) this;
    }

    /**
     * 自定义排序条件
     *
     * @param condition 执行条件
     * @param isAsc     是否正序
     * @param columns   排序字段
     * @return 排序对象
     */
    public O apply(boolean condition, boolean isAsc, FieldMapping... columns) {
        if (condition && Predicates.notEmpty(columns)) {
            return isAsc ? this.asc(columns) : this.desc(columns);
        } else {
            return (O) this;
        }
    }

    /**
     * 自定义排序条件
     *
     * @param condition 执行条件
     * @param isAsc     是否正序
     * @param columns   排序字段
     * @return 排序对象
     */
    public O apply(boolean condition, boolean isAsc, String... columns) {
        if (condition && Predicates.notEmpty(columns)) {
            return isAsc ? this.asc(columns) : this.desc(columns);
        } else {
            return (O) this;
        }
    }

    /**
     * 增加字段排序
     *
     * @param isAsc 是否顺序
     */
    private void applyField(String column, boolean isAsc) {
        if (notBlank(column)) {
            String segment = column + SPACE + (isAsc ? ASC : DESC);
            this.wrapper.getWrapperData().apply(ORDER_BY, EMPTY, segment, RETAIN);
        }
    }

    /**
     * 增加字段排序
     *
     * @param isAsc 是否顺序
     */
    void applyField(FieldMapping column, boolean isAsc) {
        if (column != null) {
            this.applyField(column.column, isAsc);
        }
    }

    @Override
    protected OrderByApply<O, W> process(FieldMapping field) {
        this.apply.setCurrentField(field);
        return this.apply;
    }
}