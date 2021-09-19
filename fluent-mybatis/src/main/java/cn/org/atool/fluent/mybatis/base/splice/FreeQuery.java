package cn.org.atool.fluent.mybatis.base.splice;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.splice.FreeSegment.GroupBy;
import cn.org.atool.fluent.mybatis.base.splice.FreeSegment.Having;
import cn.org.atool.fluent.mybatis.base.splice.FreeSegment.QueryOrderBy;
import cn.org.atool.fluent.mybatis.base.splice.FreeSegment.Selector;
import cn.org.atool.fluent.mybatis.segment.fragment.BracketFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.function.Supplier;

import static cn.org.atool.fluent.mybatis.base.splice.FreeSegment.QueryWhere;

/**
 * 字符串形式自由拼接查询器构造
 *
 * @author darui.wu
 */
@Accessors(chain = true)
@SuppressWarnings({"rawtypes"})
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

    public FreeQuery() {
        super((IFragment) null, null, EmptyEntity.class);
    }

    public FreeQuery(String table) {
        super(table == null ? null : () -> table, null, EmptyEntity.class);
    }

    public FreeQuery(String table, String alias) {
        super(table == null ? null : () -> table, alias, EmptyEntity.class);
    }

    public FreeQuery(Supplier<String> table, String alias) {
        super(table, alias, EmptyEntity.class);
    }

    public FreeQuery(IFragment table, String alias) {
        super(table, alias, EmptyEntity.class);
    }

    /**
     * 嵌套子查询 select * from (select * ...) alias;
     *
     * @param child 子查询
     * @param alias 别名
     */
    public FreeQuery(IQuery child, String alias) {
        super(BracketFrag.set(child), alias, EmptyEntity.class);
        child.data().sharedParameter(this.data);
    }

    public FreeQuery emptyQuery() {
        return new FreeQuery();
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
    public FreeQuery customizedByPlaceholder(String sql, Object parameter) {
        this.data.customizedSql(sql, parameter);
        return this;
    }

    /**
     * 完全自定义的sql
     * 使用此方法, Query的其它设置(select,where,order,group,limit等)将无效
     *
     * @param sql   用户定义的完整sql语句
     * @param paras sql参数, 通过sql中的'?'占位
     * @return self
     */
    public FreeQuery customizedByQuestion(String sql, Object... paras) {
        String placeholder = this.data.paramSql(null, sql, paras);
        this.data.customizedSql(placeholder, null);
        return this;
    }

    @Override
    public List<String> allFields() {
        throw new RuntimeException("The method is not supported by FreeQuery.");
    }
}