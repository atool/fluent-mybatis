package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.utility.MybatisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment.GROUP_BY;
import static cn.org.atool.fluent.mybatis.base.model.SqlOp.RETAIN;
import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.COMMA_SPACE;
import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.EMPTY;
import static java.util.stream.Collectors.joining;

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
    private final List<String> apply = new ArrayList<>();

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
        Stream.of(columns).filter(MybatisUtil::isNotBlank).forEach(apply::add);
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
    public G apply(FieldMeta... columns) {
        Stream.of(columns).filter(c -> c != null).map(c -> c.column).forEach(apply::add);
        return (G) this;
    }

    /**
     * 添加group by字段列表
     *
     * @param condition 成立条件
     * @param columns   字段列表
     * @return groupBy选择器
     */
    public G apply(boolean condition, FieldMeta... columns) {
        return condition ? this.apply(columns) : (G) this;
    }

    @Override
    protected G process(FieldMeta field) {
        return this.apply(field.column);
    }

    @Override
    public Q end() {
        String segment = this.apply.stream().collect(joining(COMMA_SPACE));
        this.wrapper.getWrapperData().apply(GROUP_BY, EMPTY, segment, RETAIN);
        return super.end();
    }
}