package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.and.*;
import cn.org.atool.fluent.mybatis.base.BaseQueryAnd;
import cn.org.atool.fluent.mybatis.base.BaseUpdateSet;
import cn.org.atool.fluent.mybatis.base.BaseWrapperOrder;
import cn.org.atool.fluent.mybatis.base.IProperty2Column;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP.Property;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP.Column;
import com.mybatisplus.core.conditions.AbstractWrapper;

/**
 * <p>
 * NoPrimaryEntityWrapperHelper
 * NoPrimaryEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
class NoPrimaryEntityWrapperHelper {
    public static class And<Q extends AbstractWrapper & IProperty2Column> extends BaseQueryAnd<Q> {
        public final AndObject<Integer, Q> column1;
        public final AndString<Q> column2;

        And(Q query) {
            super(query);
            this.column1 = new AndObject<>(query, Column.column_1, Property.column1);
            this.column2 = new AndString<>(query, Column.column_2, Property.column2);
        }
    }

    public static abstract class BaseOrder<Q extends AbstractWrapper & IProperty2Column, O extends BaseOrder>
            extends BaseWrapperOrder<Q> {
        public final ColumnOrder<Q, O> column1;
        public final ColumnOrder<Q, O> column2;

        public BaseOrder(Q query) {
            super(query);
            this.column1 = new ColumnOrder(query, Column.column_1, this);
            this.column2 = new ColumnOrder(query, Column.column_2, this);
        }
    }

    public static class QueryOrder extends BaseOrder<NoPrimaryEntityQuery, QueryOrder> {

        public QueryOrder(NoPrimaryEntityQuery query) {
            super(query);
        }
    }

    public static class UpdateOrder extends BaseOrder<NoPrimaryEntityUpdate, UpdateOrder> {

        public UpdateOrder(NoPrimaryEntityUpdate update) {
            super(update);
        }
    }

    public static class Set extends BaseUpdateSet<NoPrimaryEntityUpdate> {
        public final SetObject<Integer, NoPrimaryEntityUpdate> column1;
        public final SetString<NoPrimaryEntityUpdate> column2;

        public Set(NoPrimaryEntityUpdate update) {
            super(update);
            this.column1 = new SetObject<>(update, Column.column_1, Property.column1);
            this.column2 = new SetString<>(update, Column.column_2, Property.column2);
        }
    }
}
