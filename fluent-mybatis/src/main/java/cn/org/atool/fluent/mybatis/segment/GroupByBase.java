package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.model.Column;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.segment.model.ISqlSegment;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.GROUP_BY;

/**
 * BaseGroupBy
 *
 * @author darui.wu
 * @create 2020/6/21 8:09 下午
 */
public abstract class GroupByBase<
    G extends GroupByBase<G, Q>,
    Q extends IBaseQuery<?, Q>
    >
    extends BaseSegment<G, Q> {

    /**
     * 排序字段
     */
    private final List<ISqlSegment> apply = new ArrayList<>();

    protected GroupByBase(Q query) {
        super(query);
    }

    /**
     * 添加group by字段列表
     *
     * @param columns 字段列表
     * @return groupBy选择器
     */
    public G apply(String... columns) {
        for (String column : columns) {
            if (notBlank(column)) {
                Column _column = Column.column(column, this.wrapper);
                apply.add(() -> _column.wrapColumn());
            }
        }
        return (G) this;
    }

    /**
     * 添加group by字段列表
     *
     * @param condition 成立条件
     * @param columns   字段列表
     * @return groupBy选择器
     */
    public G apply(boolean condition, String... columns) {
        return condition ? this.apply(columns) : (G) this;
    }

    /**
     * 添加group by字段列表
     *
     * @param columns 字段列表
     * @return groupBy选择器
     */
    public G apply(FieldMapping... columns) {
        for (FieldMapping column : columns) {
            if (column != null) {
                Column _column = Column.column(column, this.wrapper);
                apply.add(() -> _column.wrapColumn());
            }
        }
        return (G) this;
    }

    /**
     * 添加group by字段列表
     *
     * @param condition 成立条件
     * @param columns   字段列表
     * @return groupBy选择器
     */
    public G apply(boolean condition, FieldMapping... columns) {
        return condition ? this.apply(columns) : (G) this;
    }

    @Override
    protected G apply() {
        this.apply(this.current);
        return (G) this;
    }

    @Override
    public Q end() {
        for (ISqlSegment segment : this.apply) {
            this.wrapper.getWrapperData().apply(GROUP_BY, segment);
        }
        return super.end();
    }
}