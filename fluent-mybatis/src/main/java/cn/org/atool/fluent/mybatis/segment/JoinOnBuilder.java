package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;

import java.util.ArrayList;
import java.util.List;

/**
 * 表join条件构造
 *
 * @author wudarui
 */
public class JoinOnBuilder<QL extends BaseQuery<?, QL>, QR extends BaseQuery<?, QR>> {
    private final BaseQuery<?, QL> queryLeft;

    private final BaseQuery<?, QR> queryRight;

    private final JoinType joinType;

    private final List<String> ons = new ArrayList<>();

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
            ((WhereApply<?, ?>)left).current().column,
            ((WhereApply<?, ?>)right).current().column
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
        this.ons.add(this.queryLeft.appendAlias(leftValue) + " = " + this.queryRight.appendAlias(rightValue));
        return this;
    }

    /**
     * on condition
     *
     * @param condition 条件
     * @return {@link JoinOnBuilder}
     */
    public JoinOnBuilder<QL, QR> on(String condition) {
        this.ons.add(condition.trim());
        return this;
    }

    public String table() {
        String joinTable = String.format("%s %s",
            this.joinType.join(), this.queryRight.wrapperData.getTable()
        );
        if (this.ons.isEmpty()) {
            return joinTable;
        } else {
            return joinTable + " ON " + String.join(" AND ", this.ons);
        }
    }
}