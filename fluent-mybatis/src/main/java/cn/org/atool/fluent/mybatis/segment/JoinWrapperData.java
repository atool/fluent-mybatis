package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IJoin;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.metadata.JoinType;
import cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import cn.org.atool.fluent.mybatis.segment.where.BaseWhere;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * 关联查询条件设置
 *
 * @author wudarui
 */
public class JoinWrapperData extends WrapperData implements IJoin {
    public static final String T1_ALIAS = "t1";
    public static final String T2_ALIAS = "t2";

    private BaseQuery query1;

    private BaseQuery query2;

    @Getter
    private ParameterPair parameters;

    @Setter
    private JoinType joinType = JoinType.Join;

    public JoinWrapperData(ParameterPair parameters, BaseQuery query1, BaseQuery query2) {
        this.query1 = query1;
        this.query2 = query2;
        this.parameters = parameters;
    }

    private List<String> ons = new ArrayList<>();

    public JoinWrapperData on(BaseWhere left, BaseWhere right) {
        return this.on(
            T1_ALIAS + "." + ((WhereApply) left).current().column,
            T2_ALIAS + "." + ((WhereApply) right).current().column);
    }


    public JoinWrapperData on(String left, String right) {
        this.ons.add(format(left + " = " + right));
        return this;
    }

    @Override
    public String getTable() {
        if (this.table == null) {
            this.table = format("%s AS %s %s %s AS %s ON %s",
                query1.wrapperData.getTable(), T1_ALIAS,
                this.joinType.join(),
                query2.wrapperData.getTable(), T2_ALIAS,
                this.ons.stream().collect(Collectors.joining(" AND ")));
        }
        return this.table;
    }

    @Override
    public String getSqlSelect() {
        if (this.sqlSelect.isEmpty()) {
            this.sqlSelect.addAll(this.query1.wrapperData.sqlSelect());
            this.sqlSelect.addAll(this.query2.wrapperData.sqlSelect());
            if (this.sqlSelect.isEmpty()) {
                this.query1.allFields().forEach(field -> this.sqlSelect.add(query1.alias + "." + field));
                this.query2.allFields().forEach(field -> this.sqlSelect.add(query2.alias + "." + field));
            }
        }
        return super.getSqlSelect();
    }

    @Override
    public String getWhereSql() {
        if (this.mergeSegments.getWhere().isEmpty()) {
            this.query1.wrapperData.getMergeSegments().getWhere().getSegments().forEach(this.mergeSegments.getWhere()::addAll);
            if (!this.mergeSegments.getWhere().isEmpty() &&
                !this.query2.wrapperData.getMergeSegments().getWhere().getSegments().isEmpty()) {
                this.mergeSegments.getWhere().addAll(KeyWordSegment.AND);
            }
            this.query2.wrapperData.getMergeSegments().getWhere().getSegments().forEach(this.mergeSegments.getWhere()::addAll);
        }
        return super.getWhereSql();
    }

    @Override
    public String getGroupBy() {
        if (this.mergeSegments.getGroupBy().isEmpty()) {
            this.query1.wrapperData.getMergeSegments().getGroupBy().getSegments().forEach(this.mergeSegments.getGroupBy()::addAll);
            this.query2.wrapperData.getMergeSegments().getGroupBy().getSegments().forEach(this.mergeSegments.getGroupBy()::addAll);
        }
        if (this.mergeSegments.getHaving().isEmpty()) {
            this.query1.wrapperData.getMergeSegments().getHaving().getSegments().forEach(this.mergeSegments.getHaving()::addAll);
            if (!this.mergeSegments.getHaving().isEmpty() &&
                !this.query2.wrapperData.getMergeSegments().getHaving().getSegments().isEmpty()) {
                this.mergeSegments.getHaving().addAll(KeyWordSegment.AND);
            }
            this.query2.wrapperData.getMergeSegments().getHaving().getSegments().forEach(this.mergeSegments.getHaving()::addAll);
        }
        return super.getGroupBy();
    }

    @Override
    public String getOrderBy() {
        if (this.mergeSegments.getOrderBy().isEmpty()) {
            this.query1.wrapperData.getMergeSegments().getOrderBy().getSegments().forEach(this.mergeSegments.getOrderBy()::addAll);
            this.query2.wrapperData.getMergeSegments().getOrderBy().getSegments().forEach(this.mergeSegments.getOrderBy()::addAll);
        }
        return super.getOrderBy();
    }

    @Override
    public String getUpdateStr() {
        throw new RuntimeException("not support!");
    }
}