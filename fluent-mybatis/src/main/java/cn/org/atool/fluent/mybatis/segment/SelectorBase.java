package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.FieldPredicate;
import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.stream.Stream;

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
    extends BaseSegment<SelectorApply<S, Q>, Q> {

    private final SelectorApply<S, Q> apply = new SelectorApply(this);

    protected SelectorBase(Q query) {
        super(query);
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
     * @param field  查询字段
     * @param fields 查询字段
     * @return 查询字段选择器
     */
    public S apply(FieldMapping field, FieldMapping... fields) {
        this.wrapperData().addSelectColumn(field.column);
        if (isNotEmpty(fields)) {
            Stream.of(fields)
                .filter(f -> field != null)
                .map(f -> f.column)
                .forEach(this.wrapperData()::addSelectColumn);
        }
        return (S) this;
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

    @Override
    protected SelectorApply<S, Q> process(FieldMapping field) {
        return this.apply.setCurrentField(field);
    }

    private BaseQuery getQuery() {
        return (BaseQuery) this.wrapper;
    }
}