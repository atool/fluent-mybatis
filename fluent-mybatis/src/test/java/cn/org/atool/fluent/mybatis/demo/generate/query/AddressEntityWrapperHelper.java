package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.and.*;
import cn.org.atool.fluent.mybatis.base.BaseQueryAnd;
import cn.org.atool.fluent.mybatis.base.BaseUpdateSet;
import cn.org.atool.fluent.mybatis.base.BaseWrapperOrder;
import cn.org.atool.fluent.mybatis.base.IProperty2Column;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP.Property;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP.Column;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * AddressEntityWrapperHelper
 * AddressEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
class AddressEntityWrapperHelper {
    public static class And<Q extends AbstractWrapper & IProperty2Column> extends BaseQueryAnd<Q> {
        public final AndObject<Long, Q> id;
        public final AndObject<Date, Q> gmtCreated;
        public final AndObject<Date, Q> gmtModified;
        public final AndBoolean<Q> isDeleted;
        public final AndString<Q> address;

        And(Q query) {
            super(query);
            this.id = new AndObject<>(query, Column.id, Property.id);
            this.gmtCreated = new AndObject<>(query, Column.gmt_created, Property.gmtCreated);
            this.gmtModified = new AndObject<>(query, Column.gmt_modified, Property.gmtModified);
            this.isDeleted = new AndBoolean<>(query, Column.is_deleted, Property.isDeleted);
            this.address = new AndString<>(query, Column.address, Property.address);
        }
    }

    public static abstract class BaseOrder<Q extends AbstractWrapper & IProperty2Column, O extends BaseOrder>
            extends BaseWrapperOrder<Q> {
        public final ColumnOrder<Q, O> id;
        public final ColumnOrder<Q, O> gmtCreated;
        public final ColumnOrder<Q, O> gmtModified;
        public final ColumnOrder<Q, O> isDeleted;
        public final ColumnOrder<Q, O> address;

        public BaseOrder(Q query) {
            super(query);
            this.id = new ColumnOrder(query, Column.id, this);
            this.gmtCreated = new ColumnOrder(query, Column.gmt_created, this);
            this.gmtModified = new ColumnOrder(query, Column.gmt_modified, this);
            this.isDeleted = new ColumnOrder(query, Column.is_deleted, this);
            this.address = new ColumnOrder(query, Column.address, this);
        }
    }

    public static class QueryOrder extends BaseOrder<AddressEntityQuery, QueryOrder> {

        public QueryOrder(AddressEntityQuery query) {
            super(query);
        }
    }

    public static class UpdateOrder extends BaseOrder<AddressEntityUpdate, UpdateOrder> {

        public UpdateOrder(AddressEntityUpdate update) {
            super(update);
        }
    }

    public static class Set extends BaseUpdateSet<AddressEntityUpdate> {
        public final SetObject<Long, AddressEntityUpdate> id;
        public final SetObject<Date, AddressEntityUpdate> gmtCreated;
        public final SetObject<Date, AddressEntityUpdate> gmtModified;
        public final SetBoolean<AddressEntityUpdate> isDeleted;
        public final SetString<AddressEntityUpdate> address;

        public Set(AddressEntityUpdate update) {
            super(update);
            this.id = new SetObject<>(update, Column.id, Property.id);
            this.gmtCreated = new SetObject<>(update, Column.gmt_created, Property.gmtCreated);
            this.gmtModified = new SetObject<>(update, Column.gmt_modified, Property.gmtModified);
            this.isDeleted = new SetBoolean<>(update, Column.is_deleted, Property.isDeleted);
            this.address = new SetString<>(update, Column.address, Property.address);
        }
    }
}
