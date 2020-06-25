package cn.org.atool.fluent.mybatis.tutorial.query;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;

import cn.org.atool.fluent.mybatis.tutorial.entity.UserEntity;
import cn.org.atool.fluent.mybatis.tutorial.mapping.UserMP;
import cn.org.atool.fluent.mybatis.tutorial.query.UserWrapperHelper.*;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.util.function.Function;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isNotEmpty;

/**
 * @ClassName UserQuery
 * @Description UserEntity查询（删除）条件
 *
 * @author generate code
 */
public class UserQuery extends BaseQuery<UserEntity, UserQuery> {
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
    public final OrderBy orderBy = new OrderBy(this);
    /**
     * 条件设置 where ...
     */
    public final QueryWhere where = new QueryWhere(this);

    public UserQuery(){
        super(UserMP.Table_Name, UserEntity.class, UserQuery.class);
    }

    public UserQuery(ParameterPair parameters) {
        super(UserMP.Table_Name, parameters, UserEntity.class, UserQuery.class);
    }

    @Override
    public UserQuery selectId() {
        return this.select(UserMP.id.column);
    }

/**
     * 查询字段设置
     *
     * @param by 查询字段设置器
     * @return 查询器UserQuery
     */
    public UserQuery select(Function<Selector, Selector> by){
        by.apply(this.select).toString();
        return this;
    }

    @Override
    public QueryWhere where() {
        return this.where;
    }

    @Override
    protected void validateColumn(String column) throws FluentMybatisException {
        if (isNotEmpty(column) && !UserMP.ALL_COLUMNS.contains(column)) {
            throw new FluentMybatisException("the column[" + column + "] was not found in table[" + UserMP.Table_Name + "].");
        }
    }
}