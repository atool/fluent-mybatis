package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static cn.org.atool.fluent.mybatis.base.model.FieldMapping.alias;

/**
 * 关联查询条件设置
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JoinWrapperData extends WrapperData {

    private final BaseQuery query;

    private final List<BaseQuery> queries;

    @Getter
    private final Parameters parameters;

    public JoinWrapperData(BaseQuery query, List<BaseQuery> queries, Parameters shared) {
        this.query = query;
        this.queries = queries;
        this.parameters = shared;
        this.tables.add(this.query.wrapperData.getTable());
    }

    private final List<String> tables = new ArrayList<>();

    public void addTable(String table) {
        tables.add(table);
    }

    @Override
    public String getTable() {
        return String.join(" ", this.tables);
    }

    private boolean selectMerged = false;

    @Override
    public String getSqlSelect() {
        if (selectMerged) {
            return super.getSqlSelect();
        }
        this.sqlSelect.addAll(this.query.wrapperData.sqlSelect());
        for (BaseQuery query : this.queries) {
            this.sqlSelect.addAll(query.wrapperData.sqlSelect());
        }
        if (this.sqlSelect.isEmpty()) {
            this.query.allFields().forEach(c -> this.sqlSelect.add(alias(query.tableAlias, (String) c)));
            for (BaseQuery query : this.queries) {
                query.allFields().forEach(c -> this.sqlSelect.add(alias(query.tableAlias, (String) c)));
            }
        }
        selectMerged = true;
        return super.getSqlSelect();
    }

    private boolean whereMerged = false;

    @Override
    public String getWhereSql() {
        if (whereMerged) {
            return super.getWhereSql();
        }
        this.mergeSegments.getWhere().add(KeyWordSegment.AND, this.query.wrapperData.whereSegments());
        for (BaseQuery query : this.queries) {
            this.mergeSegments.getWhere().add(KeyWordSegment.AND, query.wrapperData.whereSegments());
        }
        whereMerged = true;
        return super.getWhereSql();
    }

    private boolean groupByMerged = false;

    @Override
    public String getGroupBy() {
        if (groupByMerged) {
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
        this.groupByMerged = true;
        return super.getGroupBy();
    }

    private boolean orderByMerged = false;

    @Override
    public String getOrderBy() {
        if (orderByMerged) {
            return super.getOrderBy();
        }
        this.query.wrapperData.getMergeSegments().getOrderBy().getSegments().forEach(this.mergeSegments.getOrderBy()::addAll);
        for (BaseQuery query : this.queries) {
            query.wrapperData.getMergeSegments().getOrderBy().getSegments().forEach(this.mergeSegments.getOrderBy()::addAll);
        }
        orderByMerged = true;
        return super.getOrderBy();
    }

    @Override
    public String getUpdateStr() {
        throw new RuntimeException("not support!");
    }
}