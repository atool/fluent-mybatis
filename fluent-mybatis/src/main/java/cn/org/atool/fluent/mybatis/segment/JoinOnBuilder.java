package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.fragment.CachedFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.Column;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.JoiningFrag;
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

    final JoiningFrag ons = JoiningFrag.get().setDelimiter(" AND ").setFilter(If::notBlank);

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

    public JoinOnBuilder<QL, QR> on(String condition) {
        this.ons.add(CachedFrag.set(condition));
        return this;
    }

    /**
     * 构造 JOIN table alias ON where
     *
     * @return IFragment
     */
    public IFragment joinTableOn() {
        IFragment joinTable = this.joinType.plus(this.queryRight.data.table());
        if (this.ons.isEmpty()) {
            return joinTable;
        } else {
            return joinTable.plus(" ON ").plus(this.ons);
        }
    }
}