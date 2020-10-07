package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.FieldPredicate;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;

import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;

/**
 * BaseSelector: 查询字段构造
 *
 * @author darui.wu
 * @create 2020/6/21 3:13 下午
 */
public abstract class SelectorBase<
    S extends SelectorBase<S, Q>,
    Q extends IQuery<?, Q>
    >
    extends AggregateSegment<S, Q, S> {

    protected SelectorBase(Q query) {
        super(query);
    }

    protected SelectorBase(S selector, IAggregate aggregate) {
        super(selector, aggregate);
    }

    /**
     * 增加查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S apply(String column, String... columns) {
        this.wrapperData().addSelectColumn(column);
        if (If.notEmpty(columns)) {
            Stream.of(columns).forEach(this.wrapperData()::addSelectColumn);
        }
        return (S) this;
    }

    /**
     * 增加查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S apply(FieldMapping... columns) {
        if (If.notEmpty(columns)) {
            Stream.of(columns)
                .filter(c -> c != null)
                .map(c -> c.column)
                .forEach(this.wrapperData()::addSelectColumn);
        }
        return (S) this;
    }

    /**
     * select customized as alias
     *
     * @param customized 字段或函数
     * @param alias      别名, 为空时没有别名
     * @return 查询字段选择器
     */
    public S applyAs(String customized, String alias) {
        String select = customized + (isBlank(alias) ? EMPTY : as + alias);
        this.wrapperData().addSelectColumn(select);
        return (S) this;
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
        return (S) this;
    }

    @Override
    protected S process(FieldMapping field) {
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
        this.currField = field;
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
        if (this.currField == null) {
            return (S) this;
        }
        String expression = aggregate == null ? this.currField.column : aggregate.aggregate(this.currField.column);
        return this.applyAs(expression, alias);
    }

    private static final String as = " AS ";
}