package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.FieldPredicate;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.fragment.Column;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.JoiningFrag;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.notBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.COMMA_SPACE;
import static java.util.stream.Collectors.toList;

/**
 * BaseSelector: 查询字段构造
 *
 * @author darui.wu  2020/6/21 3:13 下午
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class SelectorBase<
    S extends SelectorBase<S, Q>,
    Q extends IBaseQuery<?, Q>
    >
    extends AggregateSegment<S, Q, S> {

    protected SelectorBase(Q query) {
        super(query);
    }

    protected SelectorBase(S origin, IAggregate aggregate) {
        super(origin, aggregate);
    }

    /**
     * 增加查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S apply(String column, String... columns) {
        assertColumns(columns);
        this.applyAs(column, null);
        Stream.of(columns).forEach(c -> this.applyAs(c, null));
        return super.getOrigin();
    }

    /**
     * 增加查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S apply(FieldMapping column, FieldMapping... columns) {
        assertColumns(columns);
        this.applyAs(column, null);
        Stream.of(columns).forEach(c -> this.applyAs(c, null));
        return super.getOrigin();
    }

    private void assertColumns(Object[] columns) {
        if (If.notEmpty(columns) && this.aggregate != null) {
            throw new RuntimeException("Aggregate functions allow only one apply column.");
        }
    }

    /**
     * 增加带别名的查询字段
     *
     * @param column 查询字段
     * @param alias  别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyAs(final String column, final String alias) {
        return this.addSelectColumn(this.aggregate, column, alias);
    }

    /**
     * 增加带别名的查询字段
     *
     * @param column 查询字段
     * @param alias  别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyAs(final FieldMapping column, final String alias) {
        return this.addSelectColumn(this.aggregate, column, alias);
    }

    /**
     * 排除查询字段, 排除方式, 无需end()结尾
     *
     * @param columns 要排除的查询字段
     * @return IQuery
     */
    public Q applyExclude(String... columns) {
        this.assertColumns(columns);
        List<String> excludes = Arrays.asList(columns);
        IFragment seg = this.excludeSelect(excludes);
        this.data().select(seg);
        return (Q) super.wrapper;
    }

    /**
     * 排除查询字段, 排除方式, 无需end()结尾
     *
     * @param columns 要排除的查询字段
     * @return IQuery
     */
    public Q applyExclude(FieldMapping... columns) {
        this.assertColumns(columns);
        List<String> excludes = Stream.of(columns).map(f -> f.column).collect(toList());
        IFragment seg = this.excludeSelect(excludes);
        this.data().select(seg);
        return (Q) super.wrapper;
    }

    public S applyFunc(final IAggregate func, final Comparable<String> column, final String alias) {
        return this.addSelectColumn(func, column, alias);
    }

    /**
     * count(*) as alias
     *
     * @param alias 别名, 为空时没有别名
     * @return 选择器
     */
    public S count(String alias) {
        return this.applyAs("count(*)", alias);
    }

    /**
     * 过滤查询的字段信息
     *
     * <p>例1: 只要 java 字段名以 "test" 开头的   -> select(i -> i.getProperty().startsWith("test"))</p>
     * <p>例2: 要全部字段                        -> select(i -> true)</p>
     * <p>例3: 只要字符串类型字段                 -> select(i -> i.getPropertyType instance String)</p>
     *
     * @param predicate 过滤方式 (主键除外!)
     * @return 字段选择器
     */
    public S apply(FieldPredicate predicate) {
        List<String> columns = this.wrapper.getTableMeta().filter(false, predicate);
        columns.forEach(this::apply);
        return super.getOrigin();
    }

    @Override
    protected S apply() {
        return this.applyAs(this.current.column, null);
    }

    /**
     * 对当前字段处理，别名处理
     *
     * @param field 字段
     * @param alias 别名
     * @return 选择器
     */
    protected S process(FieldMapping field, String alias) {
        this.current = field;
        return this.applyAs(field, alias);
    }

    private static final String AS = " AS ";

    /**
     * 排除查询的字段列表
     *
     * @param excludes 排除查询的字段列表
     * @return ignore
     */
    private IFragment excludeSelect(List<String> excludes) {
        return m -> {
            JoiningFrag joining = new JoiningFrag(COMMA_SPACE);
            for (FieldMapping f : m.allFields()) {
                if (excludes.contains(f.column)) {
                    continue;
                }
                joining.add(Column.set(this.wrapper, f));
            }
            return joining.get(m);
        };
    }

    /**
     * 添加要查询的字段
     *
     * @param aggregate 聚合函数
     * @param column    字段(column name or fieldMapping)
     * @param alias     别名
     * @return ignore
     */
    private S addSelectColumn(IAggregate aggregate, Object column, String alias) {
        IFragment frag;
        if (column instanceof FieldMapping) {
            frag = Column.set(this.wrapper, ((FieldMapping) column).column);
        } else {
            frag = Column.set(this.wrapper, String.valueOf(column));
        }
        if (aggregate != null) {
            frag = aggregate.aggregate(frag);
        }
        if (notBlank(alias)) {
            this.data().getFieldAlias().add(alias);
            frag = frag.plus(AS).plus(alias);
        }
        this.data().select(frag);
        return super.getOrigin();
    }
}