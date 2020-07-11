package cn.org.atool.fluent.mybatis.demo.generate.wrapper;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;

import cn.org.atool.fluent.mybatis.demo.generate.entity.NoPrimaryEntity;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryMapping;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoPrimaryWrapperHelper.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.util.function.Function;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotBlank;

/**
 * @ClassName NoPrimaryQuery
 * @Description NoPrimaryEntity查询（删除）条件
 *
 * @author generate code
 */
public class NoPrimaryQuery extends BaseQuery<NoPrimaryEntity, NoPrimaryQuery> {
    public final Selector select = new Selector(this);
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
    public final QueryOrderBy orderBy = new QueryOrderBy(this);
    /**
     * 条件设置 where ...
     */
    public final QueryWhere where = new QueryWhere(this);

    public NoPrimaryQuery(){
        super(NoPrimaryMapping.Table_Name, NoPrimaryEntity.class, NoPrimaryQuery.class);
    }

    public NoPrimaryQuery(ParameterPair parameters) {
        super(NoPrimaryMapping.Table_Name, parameters, NoPrimaryEntity.class, NoPrimaryQuery.class);
    }

    @Override
    public NoPrimaryQuery selectId() {
        throw new FluentMybatisException("The primary key of in table[" + NoPrimaryMapping.Table_Name + "] was not found.");
    }

    /**
     * 查询字段设置
     *
     * @param by 查询字段设置器
     * @return 查询器NoPrimaryQuery
     */
    public NoPrimaryQuery select(Function<Selector, Selector> by){
        by.apply(this.select).toString();
        return this;
    }

    @Override
    public QueryWhere where() {
        return this.where;
    }

    @Override
    protected boolean hasPrimary() {
        return false;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotBlank(column) && !NoPrimaryMapping.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + NoPrimaryMapping.Table_Name + "].");
        }
    }
}