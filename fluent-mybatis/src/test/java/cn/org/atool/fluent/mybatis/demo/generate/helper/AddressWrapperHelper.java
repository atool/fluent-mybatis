package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.demo.generate.helper.AddressMapping;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.AddressQuery;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.AddressUpdate;

/**
 * <p>
 * AddressWrapperHelper
 * AddressEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class AddressWrapperHelper {
    public interface ISegment<R> {
        R set(FieldMeta fieldMeta);

        default R id() {
            return this.set(AddressMapping.id);
        }

        default R gmtCreated() {
            return this.set(AddressMapping.gmtCreated);
        }

        default R gmtModified() {
            return this.set(AddressMapping.gmtModified);
        }

        default R isDeleted() {
            return this.set(AddressMapping.isDeleted);
        }

        default R address() {
            return this.set(AddressMapping.address);
        }

        default R userId() {
            return this.set(AddressMapping.userId);
        }
    }
    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, AddressQuery>
        implements ISegment<SelectorApply<Selector, AddressQuery>> {

        public Selector(AddressQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, AddressQuery, AddressQuery>
        implements ISegment<WhereApply<QueryWhere, AddressQuery>> {

        public QueryWhere(AddressQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, AddressUpdate, AddressQuery>
        implements ISegment<WhereApply<UpdateWhere, AddressQuery>> {

        public UpdateWhere(AddressUpdate update) {
            super(update);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, AddressQuery>
        implements ISegment<GroupBy> {

        public GroupBy(AddressQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, AddressQuery>
        implements ISegment<HavingApply<Having, AddressQuery>> {

        public Having(AddressQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class QueryOrderBy extends OrderByBase<QueryOrderBy, AddressQuery>
        implements ISegment<QueryOrderBy> {

        public QueryOrderBy(AddressQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, AddressUpdate>
        implements ISegment<UpdateOrderBy> {

        public UpdateOrderBy(AddressUpdate updator) {
            super(updator);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, AddressUpdate>
        implements ISegment<UpdateApply<UpdateSetter, AddressUpdate>> {

        public UpdateSetter(AddressUpdate update) {
            super(update);
        }
    }
}