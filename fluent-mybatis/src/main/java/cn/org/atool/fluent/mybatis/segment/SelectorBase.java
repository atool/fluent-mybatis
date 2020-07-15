package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.FieldPredicate;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.functions.IAggregate;

import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.segment.model.Aggregate.*;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

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
    extends BaseSegment<S, Q> {

    protected final IAggregate aggregate;

    public S max;

    public S min;

    public S sum;

    public S avg;

    public S count;

    public S group_concat;

    protected SelectorBase(Q query) {
        super(query);
        this.aggregate = null;
        this.max = this.aggregateSelector(MAX);
        this.min = this.aggregateSelector(MIN);
        this.sum = this.aggregateSelector(SUM);
        this.avg = this.aggregateSelector(AVG);
        this.count = this.aggregateSelector(COUNT);
        this.group_concat = this.aggregateSelector(GROUP_CONCAT);
        this.init(max).init(min).init(sum).init(avg).init(count).init(group_concat);
    }

    S init(S selector) {
        selector.max = this.max;
        selector.min = this.min;
        selector.sum = this.sum;
        selector.avg = this.avg;
        selector.count = this.count;
        selector.group_concat = this.group_concat;
        return (S) this;
    }

    protected SelectorBase(S selector, IAggregate aggregate) {
        super((Q) selector.wrapper);
        this.aggregate = aggregate;
    }


    /**
     * select customized as alias
     *
     * @param customized 自定义字段或聚合函数
     * @param alias      别名
     * @return 查询字段选择器
     */
    public S applyAs(String customized, String alias) {
        this.wrapperData().addSelectColumn(String.format("%s AS %s", customized, alias));
        return (S) this;
    }

    /**
     * 增加查询字段
     *
     * @param columns 查询字段
     * @return 查询字段选择器
     */
    public S apply(String... columns) {
        if (isNotEmpty(columns)) {
            Stream.of(columns)
                .filter(s -> isNotBlank(s))
                .forEach(this.wrapperData()::addSelectColumn);
        }
        return (S) this;
    }


    /**
     * 执行聚合函数
     *
     * @param aggregate 聚合函数
     * @param alias     as别名
     * @return 返回字段选择器
     */
    protected S applyAs(IAggregate aggregate, String alias) {
        if (this.currField == null) {
            return (S) this;
        }
        String expression = aggregate == null ? this.currField.column : aggregate.aggregate(this.currField.column);
        if (isNotBlank(alias)) {
            expression = expression + " AS " + alias;
        }
        return this.apply(expression);
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
        String selected = this.getQuery().getTableMeta().filter(false, predicate);
        return this.apply(selected);
    }

    /**
     * 构造聚合选择器
     *
     * @param aggregate
     * @return
     */
    protected abstract S aggregateSelector(IAggregate aggregate);

    @Override
    protected S process(FieldMapping field) {
        return this.applyAs(this.aggregate, null);
    }

    private BaseQuery getQuery() {
        return (BaseQuery) this.wrapper;
    }
}