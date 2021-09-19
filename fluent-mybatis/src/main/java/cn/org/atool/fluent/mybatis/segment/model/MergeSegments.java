package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag;
import cn.org.atool.fluent.mybatis.segment.list.*;
import lombok.AccessLevel;
import lombok.Setter;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.SPACE;
import static cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag.*;

/**
 * 合并 SQL 片段
 *
 * @author darui.wu
 */
public class MergeSegments extends BaseSegmentList {

    public final WhereSegmentList where = new WhereSegmentList();

    public final GroupBySegmentList groupBy = new GroupBySegmentList();

    public final HavingSegmentList having = new HavingSegmentList();

    public final OrderBySegmentList orderBy = new OrderBySegmentList();

    @Setter(AccessLevel.NONE)
    private String lastSql = EMPTY;

    /**
     * 添加sql片段
     *
     * @param keyword  关键字
     * @param segments sql片段
     */
    @Override
    public MergeSegments add(KeyFrag keyword, IFragment... segments) {
        switch (keyword) {
            case ORDER_BY:
                this.orderBy.add(ORDER_BY, segments);
                break;
            case GROUP_BY:
                this.groupBy.add(GROUP_BY, segments);
                break;
            case HAVING:
                this.having.add(HAVING, segments);
                break;
            default:
                where.add(keyword, segments);
                break;
        }
        super.cached = null;
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
    public IFragment get() {
        return where.get().plus(groupBy.get()).plus(having.get()).plus(orderBy.get()).plus(last());
    }

    public String last() {
        return isBlank(lastSql) ? EMPTY : SPACE + lastSql.trim();
    }

    public MergeSegments last(String lastSql) {
        this.lastSql = isBlank(this.lastSql) ? lastSql : this.lastSql + SPACE + lastSql;
        return this;
    }
}