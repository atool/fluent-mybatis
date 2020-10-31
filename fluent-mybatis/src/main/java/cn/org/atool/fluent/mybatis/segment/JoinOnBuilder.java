package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * 表join条件构造
 *
 * @author wudarui
 */
public class JoinOnBuilder<QL extends BaseQuery<?, QL>, QR extends BaseQuery<?, QR>> {
    private BaseQuery queryLeft;

    private BaseQuery queryRight;

    private JoinType joinType;

    private List<String> ons = new ArrayList<>();

    public JoinOnBuilder(BaseQuery queryLeft, JoinType joinType, BaseQuery queryRight) {
        this.queryLeft = queryLeft;
        this.queryRight = queryRight;
        this.joinType = joinType;
    }

    /**
     * on left = right
     *
     * @param left
     * @param right
     * @return
     */
    public JoinOnBuilder<QL, QR> on(BaseWhere left, BaseWhere right) {
        return this.on(
            ((WhereApply) left).current().alias(this.queryLeft.getAlias()),
            ((WhereApply) right).current().alias(this.queryRight.getAlias())
        );
    }

    /**
     * on left = right
     *
     * @param left
     * @param right
     * @return
     */
    public JoinOnBuilder on(String left, String right) {
        this.ons.add(format(left + " = " + right));
        return this;
    }

    public String table() {
        String joinTable = String.format("%s %s %s",
            this.joinType.join(), this.queryRight.getWrapperData().getTable(), this.queryRight.getAlias()
        );
        if (this.ons.isEmpty()) {
            return joinTable;
        } else {
            return joinTable + " ON " + String.join(" AND ", this.ons);
        }
    }
}