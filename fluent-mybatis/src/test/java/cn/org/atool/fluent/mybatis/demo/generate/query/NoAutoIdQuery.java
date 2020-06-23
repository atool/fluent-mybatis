package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.interfaces.base.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoAutoIdEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoAutoIdWrapperHelper.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.util.function.Function;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * @ClassName NoAutoIdQuery
 * @Description NoAutoIdEntity查询（删除）条件
 *
 * @author generate code
 */
public class NoAutoIdQuery extends BaseQuery<NoAutoIdEntity, NoAutoIdQuery> {
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

    public NoAutoIdQuery(){
        super(NoAutoIdMP.Table_Name, NoAutoIdEntity.class, NoAutoIdQuery.class);
    }

    public NoAutoIdQuery(ParameterPair parameters) {
        super(NoAutoIdMP.Table_Name, parameters, NoAutoIdEntity.class, NoAutoIdQuery.class);
    }

    @Override
    public NoAutoIdQuery selectId() {
        return this.select(NoAutoIdMP.id.column);
    }

/**
     * 查询字段设置
     *
     * @param by 查询字段设置器
     * @return 查询器NoAutoIdQuery
     */
    public NoAutoIdQuery select(Function<Selector, Selector> by){
        by.apply(this.selector).toString();
        return this;
    }

    @Override
    public QueryWhere where() {
        return this.where;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotEmpty(column) && !NoAutoIdMP.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + NoAutoIdMP.Table_Name + "].");
        }
    }
}