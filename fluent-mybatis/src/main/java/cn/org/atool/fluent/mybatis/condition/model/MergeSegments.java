package cn.org.atool.fluent.mybatis.condition.model;

import cn.org.atool.fluent.mybatis.condition.*;
import cn.org.atool.fluent.mybatis.interfaces.ISqlSegment;
import lombok.AccessLevel;
import lombok.Setter;

import static cn.org.atool.fluent.mybatis.condition.model.KeyWordSegment.*;
import static cn.org.atool.fluent.mybatis.condition.model.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.condition.model.StrConstant.SPACE;
import static cn.org.atool.fluent.mybatis.utility.MybatisUtil.isEmpty;

/**
 * 合并 SQL 片段
 *
 * @author darui.wu
 */
public class MergeSegments extends BaseSegmentList {

    private final WhereSegmentList where = new WhereSegmentList();

    private final GroupBySegmentList groupBy = new GroupBySegmentList();

    private final HavingSegmentList having = new HavingSegmentList();

    private final OrderBySegmentList orderBy = new OrderBySegmentList();

    @Setter(AccessLevel.NONE)
    private String lastSql = EMPTY;

    public MergeSegments setLastSql(String lastSql) {
        this.lastSql = lastSql;
        return this;
    }

    /**
     * 添加sql片段
     *
     * @param first    sql
     * @param segments sql片段
     */
    @Override
    public MergeSegments add(ISqlSegment first, ISqlSegment... segments) {
        if (first == null) {
            return this;
        }
        if (ORDER_BY == first) {
            orderBy.add(first, segments);
        } else if (GROUP_BY == first) {
            groupBy.add(first, segments);
        } else if (HAVING == first) {
            having.add(first, segments);
        } else {
            where.add(first, segments);
        }
        super.cache = null;
        return this;
    }


    /**
     * <pre>
     * 拼接sql语句 (where)
     * ... AND ...
     * group by ...
     * having ...
     * order by ...
     * last sql
     * </pre>
     *
     * @return sql
     */
    @Override
    protected String build() {
        String sql = where.sql() + groupBy.sql() + having.sql() + orderBy.sql();
        return sql.trim() + (isEmpty(lastSql) ? EMPTY : SPACE + lastSql.trim());
    }

    /**
     * 去掉orderBy部分
     *
     * @return
     */
    public String sqlNoOrderBy() {
        String sql = where.sql() + groupBy.sql() + having.sql();
        return sql.trim() + (isEmpty(lastSql) ? EMPTY : SPACE + lastSql.trim());
    }
}