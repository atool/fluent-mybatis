package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.segment.model.ColumnSegment;
import cn.org.atool.fluent.mybatis.segment.model.ISqlSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.GROUP_BY;

/**
 * BaseGroupBy
 *
 * @author darui.wu
 * @create 2020/6/21 8:09 下午
 */
public abstract class GroupByBase<
    G extends GroupByBase<G, Q>,
    Q extends IQuery<?, Q>
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
        Stream.of(columns).filter(If::notBlank).forEach(c -> apply.add(() -> c));
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
        Stream.of(columns).filter(c -> c != null)
            .map(this::columnWithAlias)
            .map(ColumnSegment::column)
            .forEach(apply::add);
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