package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.entity.IMapping;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.FieldPredicate;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.fragment.Column;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.notBlank;

/**
 * BaseSelector: 查询字段构造
 *
 * @author darui.wu  2020/6/21 3:13 下午
 */
@SuppressWarnings({"rawtypes"})
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
        if (If.notEmpty(columns) && this.aggregate != null) {
            throw new RuntimeException("Aggregate functions allow only one column.");
        }
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
    public S apply(FieldMapping... columns) {
        if (If.isEmpty(columns)) {
            throw new RuntimeException("Apply column missing.");
        } else if (this.aggregate != null && columns.length > 1) {
            throw new RuntimeException("Aggregate functions allow only one column.");
        }
        for (FieldMapping column : columns) {
            Column _column = Column.set(this.wrapper, column);
            this.addSelectSeg(this.aggregate, _column, null);
        }
        return super.getOrigin();
    }

    /**
     * 排除查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S applyNot(FieldMapping... columns) {
        if (If.isEmpty(columns)) {
            throw new RuntimeException("Apply column missing.");
        } else if (this.aggregate != null && columns.length > 1) {
            throw new RuntimeException("Aggregate functions allow only one column.");
        }
        IMapping mapping = ((Optional<IMapping>) this.wrapper.mapping())
            .orElseThrow(() -> new RuntimeException(""));

        List<FieldMapping> excludes = Arrays.asList(columns);
        List<FieldMapping> fields = mapping.allFields();
        fields.removeAll(Arrays.asList(columns));
        head:
        for (FieldMapping field : fields) {

            for (FieldMapping column : columns) {
                if (column.column.equals(field)) {
                    continue head;
                }
            }
            this.addSelectSeg(null, Column.set(this.wrapper, field), null);
        }
        return super.getOrigin();
    }

    /**
     * 增加带别名的查询字段
     *
     * @param field 查询字段
     * @param alias 别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyAs(FieldMapping field, String alias) {
        Column column = Column.set(this.wrapper, field);
        this.addSelectSeg(this.aggregate, column, alias);
        return super.getOrigin();
    }

    /**
     * 增加带别名的查询字段
     *
     * @param column 查询字段
     * @param alias  别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyAs(final String column, final String alias) {
        Column _column = Column.set(this.wrapper, column);
        this.addSelectSeg(this.aggregate, _column, alias);
        return super.getOrigin();
    }

    public S applyFunc(final IAggregate func, final String column, final String alias) {
        Column _column = Column.set(this.wrapper, column);
        this.addSelectSeg(func, _column, alias);
        return super.getOrigin();
    }

    private void addSelectSeg(IAggregate aggregate, Column column, String alias) {
        IFragment frag = column;
        if (aggregate != null) {
            frag = aggregate.aggregate(column);
        }
        if (notBlank(alias)) {
            this.data().getFieldAlias().add(alias);
            frag = frag.plus(AS).plus(alias);
        }
        this.data().addSelectColumn(frag);
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
}