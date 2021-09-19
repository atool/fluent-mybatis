package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.segment.fragment.Column;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.JoiningFrag;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag.GROUP_BY;

/**
 * BaseGroupBy
 *
 * @author darui.wu 2020/6/21 8:09 下午
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class GroupByBase<
    G extends GroupByBase<G, Q>,
    Q extends IBaseQuery<?, Q>
    >
    extends BaseSegment<G, Q> {

    /**
     * 排序字段
     */
    private final JoiningFrag apply = JoiningFrag.get();

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
                apply.add(Column.set(this.wrapper, column));
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
                apply.add(Column.set(this.wrapper, column));
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
        for (IFragment segment : this.apply.getSegments()) {
            this.wrapper.data().apply(GROUP_BY, segment);
        }
        return super.end();
    }
}