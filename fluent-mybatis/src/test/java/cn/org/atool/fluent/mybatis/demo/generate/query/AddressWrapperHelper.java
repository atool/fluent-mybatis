package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.condition.apply.*;
import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.demo.generate.entity.AddressEntity;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP.Property;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP.Column;
import java.util.Date;

/**
 * <p>
 * AddressWrapperHelper
 * AddressEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class AddressWrapperHelper {
    /**
     * select字段设置
     */
    public static final class Selector extends BaseSelector<Selector>{
        public final ColumnSelector<Selector> id = new ColumnSelector<>(this, Column.id);
        public final ColumnSelector<Selector> gmtCreated = new ColumnSelector<>(this, Column.gmt_created);
        public final ColumnSelector<Selector> gmtModified = new ColumnSelector<>(this, Column.gmt_modified);
        public final ColumnSelector<Selector> isDeleted = new ColumnSelector<>(this, Column.is_deleted);
        public final ColumnSelector<Selector> address = new ColumnSelector<>(this, Column.address);

        Selector(AddressQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     * @param <W> 更新器或查询器
     */
    public static final class WrapperWhere<W extends Wrapper<AddressEntity, W, AddressQuery>>
        extends BaseWhere<AddressEntity, W, AddressQuery> {
        public final WhereObject<AddressEntity, Long, W, AddressQuery> id = new WhereObject<>(this, Column.id, Property.id);
        public final WhereObject<AddressEntity, Date, W, AddressQuery> gmtCreated = new WhereObject<>(this, Column.gmt_created, Property.gmtCreated);
        public final WhereObject<AddressEntity, Date, W, AddressQuery> gmtModified = new WhereObject<>(this, Column.gmt_modified, Property.gmtModified);
        public final WhereBoolean<AddressEntity, W, AddressQuery> isDeleted = new WhereBoolean<>(this, Column.is_deleted, Property.isDeleted);
        public final WhereString<AddressEntity, W, AddressQuery> address = new WhereString<>(this, Column.address, Property.address);

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
        public final GroupBy<QueryGroup> address = new GroupBy<>(this, Column.address);

        QueryGroup(AddressQuery query) {
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
        public final HavingBy<Having> address = new HavingBy<>(this, Column.address);

        Having(AddressQuery query) {
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
        public final OrderBy<QueryOrder> address = new OrderBy<>(this, Column.address);

        QueryOrder(AddressQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends BaseSetter<AddressEntity, AddressUpdate> {
        public final SetObject<Long, AddressUpdate> id = new SetObject<>(this, Column.id, Property.id);
        public final SetObject<Date, AddressUpdate> gmtCreated = new SetObject<>(this, Column.gmt_created, Property.gmtCreated);
        public final SetObject<Date, AddressUpdate> gmtModified = new SetObject<>(this, Column.gmt_modified, Property.gmtModified);
        public final SetBoolean<AddressUpdate> isDeleted = new SetBoolean<>(this, Column.is_deleted, Property.isDeleted);
        public final SetString<AddressUpdate> address = new SetString<>(this, Column.address, Property.address);

        UpdateSetter(AddressUpdate update) {
            super(update);
        }
    }
}