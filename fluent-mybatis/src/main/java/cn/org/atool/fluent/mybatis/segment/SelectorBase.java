package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.IBaseQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.FieldPredicate;
import cn.org.atool.fluent.mybatis.functions.IAggregate;

import java.util.Objects;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.notBlank;

/**
 * BaseSelector: 查询字段构造
 *
 * @author darui.wu
 * @create 2020/6/21 3:13 下午
 */
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
        if (this.aggregate != null) {
            if (columns == null || columns.length > 0) {
                throw new RuntimeException("Aggregate functions allow only one column.");
            }
            String expression = aggregate.aggregate(column);
            return this.applyAs(expression, null);
        } else {
            this.wrapperData().addSelectColumn(column);
            if (If.notEmpty(columns)) {
                Stream.of(columns).forEach(this.wrapperData()::addSelectColumn);
            }
            return super.getOrigin();
        }
    }

    /**
     * 增加查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S apply(FieldMapping... columns) {
        if (columns == null || columns.length == 0) {
            throw new RuntimeException("Apply column missing.");
        }
        if (this.aggregate != null) {
            if (columns.length != 1) {
                throw new RuntimeException("Aggregate functions allow only one column.");
            }
            this.current = columns[0];
            return this.apply(this.aggregate, null);
        } else {
            Stream.of(columns)
                .filter(Objects::nonNull)
                .map(this::columnWithAlias)
                .forEach(this.wrapperData()::addSelectColumn);
            return super.getOrigin();
        }
    }

    /**
     * 增加带别名的查询字段
     *
     * @param field   查询字段
     * @param alias   别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyAs(FieldMapping field, String alias) {
        return applyAs(field.column, alias);
    }

    /**
     * 增加带别名的查询字段
     *
     * @param column  查询字段
     * @param alias   别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyAs(String column, String alias) {
        String select = column;
        if (notBlank(this.wrapper.alias) && BaseWrapperHelper.isColumnName(column)) {
            select = this.wrapper.alias + "." + column;
        }
        if (notBlank(alias)) {
            select = select + AS + alias;
        }
        this.wrapperData().addSelectColumn(select);
        return super.getOrigin();
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
        String select = this.wrapper.getTableMeta().filter(false, predicate);
        this.wrapperData().addSelectColumn(select);
        return super.getOrigin();
    }

    @Override
    protected S apply() {
        return this.apply(this.aggregate, null);
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
        return this.apply(this.aggregate, alias);
    }

    /**
     * 执行聚合函数
     *
     * @param aggregate 聚合函数
     * @param alias     as别名, 为空时没有别名
     * @return 返回字段选择器
     */
    private S apply(IAggregate aggregate, String alias) {
        if (this.current == null) {
            return this.origin == null ? (S) this : this.origin;
        }
        String expression = aggregate == null ? this.currentWithAlias() : aggregate.aggregate(this.currentWithAlias());
        return this.applyAs(expression, alias);
    }

    private static final String AS = " AS ";
}
