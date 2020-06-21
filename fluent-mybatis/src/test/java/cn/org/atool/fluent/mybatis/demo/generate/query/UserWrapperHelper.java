package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.apply.*;
import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.demo.generate.entity.UserEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP.Property;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP.Column;
import java.util.Date;

/**
 * <p>
 * UserWrapperHelper
 * UserEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class UserWrapperHelper {
    /**
     * select字段设置
     */
    public static final class Selector extends BaseSelector<Selector>{
        public final ColumnSelector<Selector> id = new ColumnSelector<>(this, Column.id);
        public final ColumnSelector<Selector> gmtCreated = new ColumnSelector<>(this, Column.gmt_created);
        public final ColumnSelector<Selector> gmtModified = new ColumnSelector<>(this, Column.gmt_modified);
        public final ColumnSelector<Selector> isDeleted = new ColumnSelector<>(this, Column.is_deleted);
        public final ColumnSelector<Selector> addressId = new ColumnSelector<>(this, Column.address_id);
        public final ColumnSelector<Selector> age = new ColumnSelector<>(this, Column.age);
        public final ColumnSelector<Selector> userName = new ColumnSelector<>(this, Column.user_name);
        public final ColumnSelector<Selector> version = new ColumnSelector<>(this, Column.version);

        Selector(UserQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     * @param <W> 更新器或查询器
     */
    public static final class WrapperWhere<W extends Wrapper<UserEntity, W, UserQuery>>
        extends BaseWhere<UserEntity, W, UserQuery> {
        public final WhereObject<UserEntity, Long, W, UserQuery> id = new WhereObject<>(this, Column.id, Property.id);
        public final WhereObject<UserEntity, Date, W, UserQuery> gmtCreated = new WhereObject<>(this, Column.gmt_created, Property.gmtCreated);
        public final WhereObject<UserEntity, Date, W, UserQuery> gmtModified = new WhereObject<>(this, Column.gmt_modified, Property.gmtModified);
        public final WhereBoolean<UserEntity, W, UserQuery> isDeleted = new WhereBoolean<>(this, Column.is_deleted, Property.isDeleted);
        public final WhereObject<UserEntity, Long, W, UserQuery> addressId = new WhereObject<>(this, Column.address_id, Property.addressId);
        public final WhereObject<UserEntity, Integer, W, UserQuery> age = new WhereObject<>(this, Column.age, Property.age);
        public final WhereString<UserEntity, W, UserQuery> userName = new WhereString<>(this, Column.user_name, Property.userName);
        public final WhereString<UserEntity, W, UserQuery> version = new WhereString<>(this, Column.version, Property.version);

        WrapperWhere(W wrapper) {
            this(wrapper, true);
        }
        WrapperWhere(W wrapper, boolean and) {
            super(wrapper, and);
        }
    }

    /**
     * 分组设置
     */
    public static final class QueryGroup extends BaseGroup<QueryGroup> {
        public final GroupBy<QueryGroup> id = new GroupBy<>(this, Column.id);
        public final GroupBy<QueryGroup> gmtCreated = new GroupBy<>(this, Column.gmt_created);
        public final GroupBy<QueryGroup> gmtModified = new GroupBy<>(this, Column.gmt_modified);
        public final GroupBy<QueryGroup> isDeleted = new GroupBy<>(this, Column.is_deleted);
        public final GroupBy<QueryGroup> addressId = new GroupBy<>(this, Column.address_id);
        public final GroupBy<QueryGroup> age = new GroupBy<>(this, Column.age);
        public final GroupBy<QueryGroup> userName = new GroupBy<>(this, Column.user_name);
        public final GroupBy<QueryGroup> version = new GroupBy<>(this, Column.version);

        QueryGroup(UserQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends BaseHaving<Having> {
        public final HavingBy<Having> id = new HavingBy<>(this, Column.id);
        public final HavingBy<Having> gmtCreated = new HavingBy<>(this, Column.gmt_created);
        public final HavingBy<Having> gmtModified = new HavingBy<>(this, Column.gmt_modified);
        public final HavingBy<Having> isDeleted = new HavingBy<>(this, Column.is_deleted);
        public final HavingBy<Having> addressId = new HavingBy<>(this, Column.address_id);
        public final HavingBy<Having> age = new HavingBy<>(this, Column.age);
        public final HavingBy<Having> userName = new HavingBy<>(this, Column.user_name);
        public final HavingBy<Having> version = new HavingBy<>(this, Column.version);

        Having(UserQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class QueryOrder extends BaseOrder<QueryOrder> {
        public final OrderBy<QueryOrder> id = new OrderBy<>(this, Column.id);
        public final OrderBy<QueryOrder> gmtCreated = new OrderBy<>(this, Column.gmt_created);
        public final OrderBy<QueryOrder> gmtModified = new OrderBy<>(this, Column.gmt_modified);
        public final OrderBy<QueryOrder> isDeleted = new OrderBy<>(this, Column.is_deleted);
        public final OrderBy<QueryOrder> addressId = new OrderBy<>(this, Column.address_id);
        public final OrderBy<QueryOrder> age = new OrderBy<>(this, Column.age);
        public final OrderBy<QueryOrder> userName = new OrderBy<>(this, Column.user_name);
        public final OrderBy<QueryOrder> version = new OrderBy<>(this, Column.version);

        QueryOrder(UserQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends BaseSetter<UserEntity, UserUpdate> {
        public final SetObject<Long, UserUpdate> id = new SetObject<>(this, Column.id, Property.id);
        public final SetObject<Date, UserUpdate> gmtCreated = new SetObject<>(this, Column.gmt_created, Property.gmtCreated);
        public final SetObject<Date, UserUpdate> gmtModified = new SetObject<>(this, Column.gmt_modified, Property.gmtModified);
        public final SetBoolean<UserUpdate> isDeleted = new SetBoolean<>(this, Column.is_deleted, Property.isDeleted);
        public final SetObject<Long, UserUpdate> addressId = new SetObject<>(this, Column.address_id, Property.addressId);
        public final SetObject<Integer, UserUpdate> age = new SetObject<>(this, Column.age, Property.age);
        public final SetString<UserUpdate> userName = new SetString<>(this, Column.user_name, Property.userName);
        public final SetString<UserUpdate> version = new SetString<>(this, Column.version, Property.version);

        UpdateSetter(UserUpdate update) {
            super(update);
        }
    }
}