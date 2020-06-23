package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.AddressMP;

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
            return this.set(AddressMP.id);
        }

        default R gmtCreated() {
            return this.set(AddressMP.gmtCreated);
        }

        default R gmtModified() {
            return this.set(AddressMP.gmtModified);
        }

        default R isDeleted() {
            return this.set(AddressMP.isDeleted);
        }

        default R address() {
            return this.set(AddressMP.address);
        }
    }
    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, AddressQuery>
        implements ISegment<SelectorApply<Selector, AddressQuery>> {

        Selector(AddressQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, AddressQuery, AddressQuery>
        implements ISegment<WhereApply<QueryWhere, AddressQuery>> {

        QueryWhere(AddressQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, AddressUpdate, AddressQuery>
        implements ISegment<WhereApply<UpdateWhere, AddressQuery>> {

        UpdateWhere(AddressUpdate update) {
            super(update);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, AddressQuery>
        implements ISegment<GroupBy> {

        GroupBy(AddressQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, AddressQuery>
        implements ISegment<HavingApply<Having, AddressQuery>> {

        Having(AddressQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class OrderBy extends OrderByBase<OrderBy, AddressQuery>
        implements ISegment<OrderBy> {

        OrderBy(AddressQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, AddressUpdate>
        implements ISegment<UpdateApply<UpdateSetter, AddressUpdate>> {

        UpdateSetter(AddressUpdate update) {
            super(update);
        }
    }
}