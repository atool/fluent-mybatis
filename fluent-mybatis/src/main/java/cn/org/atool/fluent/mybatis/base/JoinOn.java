package cn.org.atool.fluent.mybatis.base;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.JoinWrapperData;
import cn.org.atool.fluent.mybatis.segment.WhereApply;
import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * 表join条件构造
 *
 * @author wudarui
 */
public class JoinOn {
    private BaseQuery query1;

    private BaseQuery query2;

    private JoinType joinType;

    private List<String> ons = new ArrayList<>();

    public JoinOn(BaseQuery query1, JoinType joinType, BaseQuery query2) {
        this.query1 = query1;
        this.query2 = query2;
        this.joinType = joinType;
    }

    /**
     * on left = right
     *
     * @param left
     * @param right
     * @return
     */
    public JoinOn on(BaseWhere left, BaseWhere right) {
        return this.on(
            this.query1.getAlias() + "." + ((WhereApply) left).current().column,
            this.query2.getAlias() + "." + ((WhereApply) right).current().column);
    }

    /**
     * on left = right
     *
     * @param left
     * @param right
     * @return
     */
    public JoinOn on(String left, String right) {
        this.ons.add(format(left + " = " + right));
        return this;
    }

    public String table() {
        String joinTable = this.joinType.join() + " " + this.query2.getWrapperData().getTable() + " " + this.query2.getAlias();
        if (this.ons.isEmpty()) {
            return joinTable;
        } else {
            return joinTable + " ON " + String.join(" AND ", this.ons);
        }
    }
}