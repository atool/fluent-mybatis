package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.base.BaseQuery;
import cn.org.atool.fluent.mybatis.condition.model.ParameterPair;

import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP;
import cn.org.atool.fluent.mybatis.demo.generate.query.AddressWrapperHelper.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.util.function.Function;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * @ClassName AddressQuery
 * @Description AddressEntity查询（删除）条件
 *
 * @author generate code
 */
public class AddressQuery extends BaseQuery<AddressEntity, AddressQuery> {
    private final Selector selector = new Selector(this);
    /**
     * 分组：GROUP BY 字段, ...
     * 例: groupBy("id", "name")
     */
    public final GroupBy groupBy = new GroupBy(this);
    /**
     * 分组条件设置 having...
     */
    public final Having having = new Having(this);
    /**
     * 排序设置 order by ...
     */
    public final OrderBy orderBy = new OrderBy(this);
    /**
     * 条件设置 where ...
     */
    public final QueryWhere where = new QueryWhere(this);

    public AddressQuery(){
        super(AddressMP.Table_Name, AddressEntity.class, AddressQuery.class);
    }

    public AddressQuery(ParameterPair parameters) {
        super(AddressMP.Table_Name, parameters, AddressEntity.class, AddressQuery.class);
    }

    @Override
    public AddressQuery selectId() {
        return this.select(AddressMP.id.column);
    }

/**
     * 查询字段设置
     *
     * @param by 查询字段设置器
     * @return 查询器AddressQuery
     */
    public AddressQuery select(Function<Selector, Selector> by){
        by.apply(this.selector).toString();
        return this;
    }

    @Override
    public QueryWhere where() {
        return this.where;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotEmpty(column) && !AddressMP.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + AddressMP.Table_Name + "].");
        }
    }
}