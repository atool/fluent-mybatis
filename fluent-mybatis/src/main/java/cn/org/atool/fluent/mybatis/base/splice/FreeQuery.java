package cn.org.atool.fluent.mybatis.base.splice;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.GroupBy;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.Having;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.QueryOrderBy;
import cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.Selector;

import java.util.List;

import static cn.org.atool.fluent.mybatis.base.splice.FreeWrapperHelper.QueryWhere;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;

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

    public FreeQuery(String table) {
        this(table, EMPTY);
    }

    public FreeQuery(String table, String alias) {
        super(table, alias, EmptyEntity.class, FreeQuery.class);
    }

    public FreeQuery(String table, String alias, IQuery join) {
        super(table, alias, join.getWrapperData().getParameters(), EmptyEntity.class, FreeQuery.class);
    }

    public FreeQuery emptyQuery() {
        return new FreeQuery(super.table, super.alias);
    }

    @Override
    public List<String> allFields() {
        throw new RuntimeException("not support by FreeQuery.");
    }

    @Override
    public QueryWhere where() {
        return new QueryWhere(this);
    }
}