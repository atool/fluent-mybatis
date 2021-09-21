package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.fragment.CachedFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.Column;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.JoiningFrag;

/**
 * 表join条件构造
 *
 * @author wudarui
 */
@SuppressWarnings("rawtypes")
class JoinOnBuilder {
    private final BaseQuery lQuery;

    private final BaseQuery rQuery;

    private final JoinType joinType;

    final JoiningFrag ons = JoiningFrag.get().setDelimiter(" AND ").setFilter(If::notBlank);

    JoinOnBuilder(BaseQuery queryLeft, JoinType joinType, BaseQuery queryRight) {
        this.lQuery = queryLeft;
        this.rQuery = queryRight;
        this.joinType = joinType;
    }

    /**
     * on left = right,各只取最后一个调用属性
     *
     * @param lWhere 左表条件,只取最后一个调用属性
     * @param rWhere 右表条件,只取最后一个调用属性
     */
    void on(WhereApply lWhere, WhereApply rWhere) {
        this.ons.add(Column.set(lQuery, lWhere.current()).plus(" = ").plus(Column.set(rQuery, rWhere.current())));
    }

    /**
     * on leftValue = rightValue
     *
     * @param lColumn 左条件字段
     * @param rColumn 右条件字段
     */
    void on(String lColumn, String rColumn) {
        this.ons.add(Column.set(this.lQuery, lColumn).plus(" = ").plus(Column.set(this.rQuery, rColumn)));
    }

    void on(String condition) {
        this.ons.add(CachedFrag.set(condition));
    }

    /**
     * 构造 JOIN table alias ON where
     *
     * @return IFragment
     */
    IFragment joinTableOn() {
        IFragment joinTable = this.joinType.plus(this.rQuery.data.table());
        if (this.ons.isEmpty()) {
            return joinTable;
        } else {
            return joinTable.plus(" ON ").plus(this.ons);
        }
    }
}