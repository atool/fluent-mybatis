package cn.org.atool.fluent.mybatis.base.splice;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.GroupBy;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.Having;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.QueryOrderBy;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.Selector;

import java.util.List;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.QueryWhere;

/**
 * 字符串形式自由拼接查询器构造
 *
 * @author darui.wu
 */
public class FreeQuery extends BaseQuery<EmptyEntity, FreeQuery> {
    /**
     * 指定查询字段, 默认无需设置
     */
    public final Selector select = new Selector(this);

    /**
     * 分组：GROUP BY 字段, ...
     * 例: groupBy('id', 'name')
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
     * 查询条件 where ...
     */
    public final QueryWhere where = new QueryWhere(this);

    public FreeQuery(Supplier<String> table, String alias) {
        super(table, alias, EmptyEntity.class, FreeQuery.class);
    }

    public FreeQuery(String table) {
        this(() -> table, null);
    }

    public FreeQuery(String table, String alias) {
        this(() -> table, alias);
    }

    /**
     * 嵌套子查询 select * from (select * ...) alias;
     *
     * @param child
     * @param alias
     */
    public FreeQuery(IQuery child, String alias) {
        this(() -> "(" + child.getWrapperData().getQuerySql() + ")", alias);
        child.getWrapperData().sharedParameter(this.wrapperData);
    }

    /**
     * use {@link FreeQuery#FreeQuery(String, String)} directly
     */
    @Deprecated
    public FreeQuery(String table, String alias, IQuery join) {
        this(table, alias);
    }

    public FreeQuery emptyQuery() {
        return new FreeQuery(super.table, super.tableAlias);
    }

    @Override
    public List<String> allFields() {
        throw new RuntimeException("not support by FreeQuery.");
    }

    @Override
    public QueryWhere where() {
        return new QueryWhere(this);
    }

    /**
     * 完全自定义的sql
     * 使用此方法, Query的其它设置(select,where,order,group,limit等)将无效
     *
     * @param sql       用户定义的完整sql语句
     * @param parameter sql参数, 通过#{value} 或 #{field.field}占位
     * @return self
     */
    public FreeQuery customized(String sql, Object parameter) {
        this.wrapperData.customizedSql(sql, parameter);
        return this;
    }

    /**
     * 完全自定义的sql
     * 使用此方法, Query的其它设置(select,where,order,group,limit等)将无效
     *
     * @param sql 用户定义的完整sql语句
     * @return self
     */
    public FreeQuery customized(String sql) {
        return this.customized(sql, null);
    }
}