package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.BaseQuery;
import cn.org.atool.fluent.mybatis.segment.fragment.Column;
import cn.org.atool.fluent.mybatis.segment.fragment.IFragment;
import cn.org.atool.fluent.mybatis.segment.fragment.JoiningFrag;
import cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag;
import cn.org.atool.fluent.mybatis.segment.model.Parameters;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cn.org.atool.fluent.mybatis.utility.StrConstant.SPACE;
import static cn.org.atool.fluent.mybatis.segment.fragment.KeyFrag.*;

/**
 * 关联查询条件设置
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class JoinWrapperData extends WrapperData {

    private final List<BaseQuery> queries;

    public JoinWrapperData(BaseQuery query, List<BaseQuery> queries, Parameters shared) {
        super(query, shared);
        this.queries = queries;
        this.tables.add(this.wrapper.data().table());
    }

    private final JoiningFrag tables = JoiningFrag.get().setDelimiter(SPACE);

    public void addTable(IFragment table) {
        tables.add(table);
    }

    @Override
    public IFragment table() {
        return this.tables;
    }

    /**
     * 已聚合的标识
     */
    private final Set<KeyFrag> mergedFlag = new HashSet<>(8);

    @Override
    public IFragment select() {
        if (!mergedFlag.contains(SELECT)) {
            this.select.add(this.wrapper.data().select);
            for (BaseQuery query : this.queries) {
                this.select.add(query.data.select);
            }
            if (this.select.isEmpty()) {
                this.wrapper.allFields().forEach(c -> this.select.add(Column.set(wrapper, (String) c)));
                for (BaseQuery query : this.queries) {
                    query.allFields().forEach(c -> this.select.add(Column.set(query, (String) c)));
                }
            }
            mergedFlag.add(SELECT);
        }
        return super.select();
    }

    @Override
    public JoiningFrag where() {
        if (!mergedFlag.contains(WHERE)) {
            this.segments().where.add(AND, this.wrapper.data().where());
            for (BaseQuery query : this.queries) {
                this.segments().where.add(AND, query.data.where());
            }
            mergedFlag.add(WHERE);
        }
        return super.where();
    }

    @Override
    public JoiningFrag groupBy() {
        if (!mergedFlag.contains(GROUP_BY)) {
            this.segments().groupBy.add(GROUP_BY, this.wrapper.data().groupBy());
            for (BaseQuery query : this.queries) {
                this.segments().groupBy.add(GROUP_BY, query.data.groupBy());
            }
            mergedFlag.add(GROUP_BY);
        }
        return super.groupBy();
    }

    @Override
    public JoiningFrag having() {
        if (!mergedFlag.contains(HAVING)) {
            this.segments().having.add(HAVING, this.wrapper.data().having());
            for (BaseQuery query : this.queries) {
                this.segments().having.add(HAVING, query.data.having());
            }
            mergedFlag.add(HAVING);
        }
        return super.groupBy();
    }

    @Override
    public JoiningFrag orderBy() {
        if (mergedFlag.add(ORDER_BY)) {
            this.segments().orderBy.add(ORDER_BY, this.wrapper.data().orderBy());
            for (BaseQuery query : this.queries) {
                this.segments().orderBy.add(ORDER_BY, query.data.orderBy());
            }
            mergedFlag.add(ORDER_BY);
        }
        return super.orderBy();
    }

    @Override
    public IFragment update() {
        throw new RuntimeException("not support!");
    }
}