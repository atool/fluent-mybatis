package cn.org.atool.mybatis.fluent.demo.query;

import cn.org.atool.mybatis.fluent.and.*;
import cn.org.atool.mybatis.fluent.base.BaseQueryAnd;
import cn.org.atool.mybatis.fluent.base.BaseUpdateSet;
import cn.org.atool.mybatis.fluent.base.BaseWrapperOrder;
import cn.org.atool.mybatis.fluent.base.IProperty2Column;
import cn.org.atool.mybatis.fluent.demo.mapping.UserMP.Property;
import cn.org.atool.mybatis.fluent.demo.mapping.UserMP.Column;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * UserEntityWrapperHelper
 * UserEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
class UserEntityWrapperHelper {
    public static class And<Q extends AbstractWrapper & IProperty2Column> extends BaseQueryAnd<Q> {
        public final AndObject<Long, Q> id;
        public final AndString<Q> userName;
        public final AndObject<Long, Q> addressId;
        public final AndObject<Date, Q> gmtCreated;
        public final AndObject<Date, Q> gmtModified;
        public final AndBoolean<Q> isDeleted;
        public final AndObject<Integer, Q> age;
        public final AndString<Q> version;

        And(Q query) {
            super(query);
            this.id = new AndObject<>(query, Column.id, Property.id);
            this.userName = new AndString<>(query, Column.user_name, Property.userName);
            this.addressId = new AndObject<>(query, Column.address_id, Property.addressId);
            this.gmtCreated = new AndObject<>(query, Column.gmt_created, Property.gmtCreated);
            this.gmtModified = new AndObject<>(query, Column.gmt_modified, Property.gmtModified);
            this.isDeleted = new AndBoolean<>(query, Column.is_deleted, Property.isDeleted);
            this.age = new AndObject<>(query, Column.age, Property.age);
            this.version = new AndString<>(query, Column.version, Property.version);
        }
    }

    public static abstract class BaseOrder<Q extends AbstractWrapper & IProperty2Column, O extends BaseOrder>
            extends BaseWrapperOrder<Q> {
        public final ColumnOrder<Q, O> id;
        public final ColumnOrder<Q, O> userName;
        public final ColumnOrder<Q, O> addressId;
        public final ColumnOrder<Q, O> gmtCreated;
        public final ColumnOrder<Q, O> gmtModified;
        public final ColumnOrder<Q, O> isDeleted;
        public final ColumnOrder<Q, O> age;
        public final ColumnOrder<Q, O> version;

        public BaseOrder(Q query) {
            super(query);
            this.id = new ColumnOrder(query, Column.id, this);
            this.userName = new ColumnOrder(query, Column.user_name, this);
            this.addressId = new ColumnOrder(query, Column.address_id, this);
            this.gmtCreated = new ColumnOrder(query, Column.gmt_created, this);
            this.gmtModified = new ColumnOrder(query, Column.gmt_modified, this);
            this.isDeleted = new ColumnOrder(query, Column.is_deleted, this);
            this.age = new ColumnOrder(query, Column.age, this);
            this.version = new ColumnOrder(query, Column.version, this);
        }
    }

    public static class QueryOrder extends BaseOrder<UserEntityQuery, QueryOrder> {

        public QueryOrder(UserEntityQuery query) {
            super(query);
        }
    }

    public static class UpdateOrder extends BaseOrder<UserEntityUpdate, UpdateOrder> {

        public UpdateOrder(UserEntityUpdate update) {
            super(update);
        }
    }

    public static class Set extends BaseUpdateSet<UserEntityUpdate> {
        public final SetObject<Long, UserEntityUpdate> id;
        public final SetString<UserEntityUpdate> userName;
        public final SetObject<Long, UserEntityUpdate> addressId;
        public final SetObject<Date, UserEntityUpdate> gmtCreated;
        public final SetObject<Date, UserEntityUpdate> gmtModified;
        public final SetBoolean<UserEntityUpdate> isDeleted;
        public final SetObject<Integer, UserEntityUpdate> age;
        public final SetString<UserEntityUpdate> version;

        public Set(UserEntityUpdate update) {
            super(update);
            this.id = new SetObject<>(update, Column.id, Property.id);
            this.userName = new SetString<>(update, Column.user_name, Property.userName);
            this.addressId = new SetObject<>(update, Column.address_id, Property.addressId);
            this.gmtCreated = new SetObject<>(update, Column.gmt_created, Property.gmtCreated);
            this.gmtModified = new SetObject<>(update, Column.gmt_modified, Property.gmtModified);
            this.isDeleted = new SetBoolean<>(update, Column.is_deleted, Property.isDeleted);
            this.age = new SetObject<>(update, Column.age, Property.age);
            this.version = new SetString<>(update, Column.version, Property.version);
        }
    }
}
