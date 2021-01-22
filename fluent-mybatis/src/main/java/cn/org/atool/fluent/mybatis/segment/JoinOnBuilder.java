package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
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
            BaseWrapperHelper.appendAlias(((WhereApply) left).current().column, this.queryLeft),
            BaseWrapperHelper.appendAlias(((WhereApply) right).current().column, this.queryRight)
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