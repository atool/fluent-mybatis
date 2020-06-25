package cn.org.atool.fluent.mybatis.tutorial.query;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.tutorial.mapping.ReceivingAddressMP;

/**
 * <p>
 * ReceivingAddressWrapperHelper
 * ReceivingAddressEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class ReceivingAddressWrapperHelper {
    public interface ISegment<R> {
        R set(FieldMeta fieldMeta);

        default R id() {
            return this.set(ReceivingAddressMP.id);
        }

        default R gmtModified() {
            return this.set(ReceivingAddressMP.gmtModified);
        }

        default R isDeleted() {
            return this.set(ReceivingAddressMP.isDeleted);
        }

        default R city() {
            return this.set(ReceivingAddressMP.city);
        }

        default R detailAddress() {
            return this.set(ReceivingAddressMP.detailAddress);
        }

        default R district() {
            return this.set(ReceivingAddressMP.district);
        }

        default R gmtCreate() {
            return this.set(ReceivingAddressMP.gmtCreate);
        }

        default R province() {
            return this.set(ReceivingAddressMP.province);
        }

        default R userId() {
            return this.set(ReceivingAddressMP.userId);
        }
    }
    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, ReceivingAddressQuery>
        implements ISegment<SelectorApply<Selector, ReceivingAddressQuery>> {

        Selector(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, ReceivingAddressQuery, ReceivingAddressQuery>
        implements ISegment<WhereApply<QueryWhere, ReceivingAddressQuery>> {

        QueryWhere(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, ReceivingAddressUpdate, ReceivingAddressQuery>
        implements ISegment<WhereApply<UpdateWhere, ReceivingAddressQuery>> {

        UpdateWhere(ReceivingAddressUpdate update) {
            super(update);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, ReceivingAddressQuery>
        implements ISegment<GroupBy> {

        GroupBy(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, ReceivingAddressQuery>
        implements ISegment<HavingApply<Having, ReceivingAddressQuery>> {

        Having(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class OrderBy extends OrderByBase<OrderBy, ReceivingAddressQuery>
        implements ISegment<OrderBy> {

        OrderBy(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, ReceivingAddressUpdate>
        implements ISegment<UpdateApply<UpdateSetter, ReceivingAddressUpdate>> {

        UpdateSetter(ReceivingAddressUpdate update) {
            super(update);
        }
    }
}