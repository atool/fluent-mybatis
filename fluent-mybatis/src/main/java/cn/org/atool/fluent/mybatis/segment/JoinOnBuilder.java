package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.fragment.*;
import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;

/**
 * 表join条件构造
 *
 * @author wudarui
 */
public class JoinOnBuilder<QL extends BaseQuery<?, QL>, QR extends BaseQuery<?, QR>> {
    private final BaseQuery<?, QL> queryLeft;

    private final BaseQuery<?, QR> queryRight;

    private final JoinType joinType;

    private final JoiningFrag ons = JoiningFrag.get();

    public JoinOnBuilder(BaseQuery<?, QL> queryLeft, JoinType joinType, BaseQuery<?, QR> queryRight) {
        this.queryLeft = queryLeft;
        this.queryRight = queryRight;
        this.joinType = joinType;
    }

    /**
     * on left = right,各只取最后一个调用属性
     *
     * @param left  左表条件,只取最后一个调用属性
     * @param right 右表条件,只取最后一个调用属性
     * @return {@link JoinOnBuilder}
     */
    public JoinOnBuilder<QL, QR> on(BaseWhere<?, QL> left, BaseWhere<?, QR> right) {
        return this.on(
            ((WhereApply<?, ?>) left).current().column,
            ((WhereApply<?, ?>) right).current().column
        );
    }

    /**
     * on leftValue = rightValue
     *
     * @param leftValue  左
     * @param rightValue 右
     * @return {@link JoinOnBuilder}
     */
    public JoinOnBuilder<QL, QR> on(String leftValue, String rightValue) {
        this.ons.add(Column.set(this.queryLeft, leftValue).plus(" = ").plus(Column.set(this.queryRight, rightValue)));
        return this;
    }

    /**
     * on condition
     *
     * @param condition 条件
     * @return {@link JoinOnBuilder}
     */
    public JoinOnBuilder<QL, QR> on(IFragment condition) {
        this.ons.add(condition);
        return this;
    }

    public JoinOnBuilder<QL, QR> on(String condition) {
        this.ons.add(CachedFrag.set(condition));
        return this;
    }

    public IFragment table() {
        FormatFrag joinTable = FormatFrag.format("%s %s", this.joinType.join(), this.queryRight.data.table());
        if (this.ons.isEmpty()) {
            return joinTable;
        } else {
            return joinTable.plus(" ON ").plus(this.ons.setDelimiter(" AND "));
        }
    }
}