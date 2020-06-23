package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.segment.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;
import cn.org.atool.fluent.mybatis.demo.generate.query.NoPrimaryWrapperHelper.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.util.function.Function;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * @ClassName NoPrimaryQuery
 * @Description NoPrimaryEntity查询（删除）条件
 *
 * @author generate code
 */
public class NoPrimaryQuery extends BaseQuery<NoPrimaryEntity, NoPrimaryQuery> {
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

    public NoPrimaryQuery(){
        super(NoPrimaryMP.Table_Name, NoPrimaryEntity.class, NoPrimaryQuery.class);
    }

    public NoPrimaryQuery(ParameterPair parameters) {
        super(NoPrimaryMP.Table_Name, parameters, NoPrimaryEntity.class, NoPrimaryQuery.class);
    }

    @Override
    public NoPrimaryQuery selectId() {
        throw new FluentMybatisException("The primary key of in table[" + NoPrimaryMP.Table_Name + "] was not found.");
    }

/**
     * 查询字段设置
     *
     * @param by 查询字段设置器
     * @return 查询器NoPrimaryQuery
     */
    public NoPrimaryQuery select(Function<Selector, Selector> by){
        by.apply(this.selector).toString();
        return this;
    }

    @Override
    public QueryWhere where() {
        return this.where;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotEmpty(column) && !NoPrimaryMP.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + NoPrimaryMP.Table_Name + "].");
        }
    }
}