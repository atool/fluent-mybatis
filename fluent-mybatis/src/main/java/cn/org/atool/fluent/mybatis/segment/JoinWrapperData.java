package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 关联查询条件设置
 *
 * @author wudarui
 */
public class JoinWrapperData extends WrapperData {
    public static final String T1_ALIAS = "t1";
    public static final String T2_ALIAS = "t2";

    private final BaseQuery query;

    private final List<BaseQuery> queries;

    @Getter
    private final ParameterPair parameters;

    public JoinWrapperData(BaseQuery query, List<BaseQuery> queries) {
        this.query = query;
        this.queries = queries;
        this.parameters = query.wrapperData.getParameters();
        this.tables.add(this.query.wrapperData.getTable() + " " + this.query.getAlias());
    }

    private List<String> tables = new ArrayList<>();

    public void addTable(String table) {
        tables.add(table);
    }

    @Override
    public String getTable() {
        return String.join(" ", this.tables);
    }

    @Override
    public String getSqlSelect() {
        if (!this.sqlSelect.isEmpty()) {
            return super.getSqlSelect();
        }
        this.sqlSelect.addAll(this.query.wrapperData.sqlSelect());
        for (BaseQuery query : this.queries) {
            this.sqlSelect.addAll(query.wrapperData.sqlSelect());
        }
        if (this.sqlSelect.isEmpty()) {
            this.query.allFields().forEach(field -> this.sqlSelect.add(query.alias + "." + field));
            for (BaseQuery query : this.queries) {
                query.allFields().forEach(field -> this.sqlSelect.add(query.alias + "." + field));
            }
        }
        return super.getSqlSelect();
    }

    @Override
    public String getWhereSql() {
        if (!this.mergeSegments.getWhere().isEmpty()) {
            return super.getWhereSql();
        }
        this.query.wrapperData.getMergeSegments().getWhere().getSegments().forEach(this.mergeSegments.getWhere()::addAll);
        for (BaseQuery query : this.queries) {
            if (!this.mergeSegments.getWhere().isEmpty() &&
                !query.wrapperData.getMergeSegments().getWhere().getSegments().isEmpty()) {
                this.mergeSegments.getWhere().addAll(KeyWordSegment.AND);
            }
            query.wrapperData.getMergeSegments().getWhere().getSegments().forEach(this.mergeSegments.getWhere()::addAll);
        }
        return super.getWhereSql();
    }

    private boolean groupBy = false;

    @Override
    public String getGroupBy() {
        if (groupBy) {
            return super.getGroupBy();
        }
        this.query.wrapperData.getMergeSegments().getGroupBy().getSegments().forEach(this.mergeSegments.getGroupBy()::addAll);
        this.query.wrapperData.getMergeSegments().getHaving().getSegments().forEach(this.mergeSegments.getHaving()::addAll);
        for (BaseQuery query : this.queries) {
            query.wrapperData.getMergeSegments().getGroupBy().getSegments().forEach(this.mergeSegments.getGroupBy()::addAll);
            if (!this.mergeSegments.getHaving().isEmpty() &&
                !query.wrapperData.getMergeSegments().getHaving().getSegments().isEmpty()) {
                this.mergeSegments.getHaving().addAll(KeyWordSegment.AND);
            }
            query.wrapperData.getMergeSegments().getHaving().getSegments().forEach(this.mergeSegments.getHaving()::addAll);
        }
        this.groupBy = true;
        return super.getGroupBy();
    }

    private boolean orderBy = false;

    @Override
    public String getOrderBy() {
        if (orderBy) {
            return super.getOrderBy();
        }
        this.query.wrapperData.getMergeSegments().getOrderBy().getSegments().forEach(this.mergeSegments.getOrderBy()::addAll);
        for (BaseQuery query : this.queries) {
            query.wrapperData.getMergeSegments().getOrderBy().getSegments().forEach(this.mergeSegments.getOrderBy()::addAll);
        }
        orderBy = true;
        return super.getOrderBy();
    }

    @Override
    public String getUpdateStr() {
        throw new RuntimeException("not support!");
    }
}