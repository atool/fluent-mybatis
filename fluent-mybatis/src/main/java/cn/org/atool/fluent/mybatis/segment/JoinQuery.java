package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.IQuery;
import cn.org.atool.fluent.mybatis.base.impl.BaseQuery;
import cn.org.atool.fluent.mybatis.functions.JoinOnConsumer;
import cn.org.atool.fluent.mybatis.segment.model.ColumnSegment;
import cn.org.atool.fluent.mybatis.segment.model.KeyWordSegment;
import cn.org.atool.fluent.mybatis.segment.model.ParameterPair;
import cn.org.atool.fluent.mybatis.segment.model.WrapperData;

public class JoinQuery<Q1 extends BaseQuery<?, Q1>, Q2 extends BaseQuery<?, Q2>>
    implements IQuery<IEntity, JoinQuery<Q1, Q2>> {
    private final Q1 query1;
    private final Q2 query2;

    private WrapperData wrapperData;

    public JoinQuery(Q1 query1, Q2 query2, JoinOnConsumer<Q1, Q2> on) {
        this.query1 = query1;
        this.query2 = query2;
        JoinOn join = new JoinOn();
        on.accept(this.query1.newEmpty(), this.query2.newEmpty(), join);
        WrapperData w1 = this.query1.getWrapperData();
        WrapperData w2 = this.query2.getWrapperData();

        String table = String.format("%s AS t1 JOIN %s AS t2 ON %s",
            w1.getTable(), w2.getTable(), join.where());
        this.wrapperData = new WrapperData(table, new ParameterPair(), IEntity.class, JoinQuery.class);

        w1.sqlSelect().forEach(this.wrapperData::addSelectColumn);
        w2.sqlSelect().forEach(this.wrapperData::addSelectColumn);

        w1.getMergeSegments().getWhere().getSegments().forEach(seg -> {
            if (seg instanceof ColumnSegment) {

            } else {
                this.wrapperData.getMergeSegments().getWhere().addAll(seg);
            }
        });
        this.wrapperData.getMergeSegments().getWhere().addAll(KeyWordSegment.AND);
        w2.getMergeSegments().getWhere().getSegments().forEach(seg -> {

            this.wrapperData.getMergeSegments().getWhere().addAll(seg);
        });
    }

    @Override
    public JoinQuery<Q1, Q2> distinct() {
        this.wrapperData.setDistinct(true);
        return this;
    }

    @Override
    public JoinQuery<Q1, Q2> selectId() {
        throw new RuntimeException("not support");
    }

    @Override
    public JoinQuery<Q1, Q2> limit(int limit) {
        return null;
    }

    @Override
    public JoinQuery<Q1, Q2> limit(int start, int limit) {
        return null;
    }

    @Override
    public JoinQuery<Q1, Q2> last(String lastSql) {
        return null;
    }

    @Override
    public WhereBase<?, JoinQuery<Q1, Q2>, JoinQuery<Q1, Q2>> where() {
        throw new RuntimeException("not support");
    }

    @Override
    public WrapperData getWrapperData() {
        return this.wrapperData;
    }
}
