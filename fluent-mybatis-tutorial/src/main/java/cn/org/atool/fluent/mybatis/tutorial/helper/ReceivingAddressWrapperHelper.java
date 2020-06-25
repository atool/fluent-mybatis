package cn.org.atool.fluent.mybatis.tutorial.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.tutorial.helper.ReceivingAddressMapping;
import cn.org.atool.fluent.mybatis.tutorial.wrapper.ReceivingAddressQuery;
import cn.org.atool.fluent.mybatis.tutorial.wrapper.ReceivingAddressUpdate;

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
            return this.set(ReceivingAddressMapping.id);
        }

        default R gmtModified() {
            return this.set(ReceivingAddressMapping.gmtModified);
        }

        default R isDeleted() {
            return this.set(ReceivingAddressMapping.isDeleted);
        }

        default R city() {
            return this.set(ReceivingAddressMapping.city);
        }

        default R detailAddress() {
            return this.set(ReceivingAddressMapping.detailAddress);
        }

        default R district() {
            return this.set(ReceivingAddressMapping.district);
        }

        default R gmtCreate() {
            return this.set(ReceivingAddressMapping.gmtCreate);
        }

        default R province() {
            return this.set(ReceivingAddressMapping.province);
        }

        default R userId() {
            return this.set(ReceivingAddressMapping.userId);
        }
    }
    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, ReceivingAddressQuery>
        implements ISegment<SelectorApply<Selector, ReceivingAddressQuery>> {

        public Selector(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, ReceivingAddressQuery, ReceivingAddressQuery>
        implements ISegment<WhereApply<QueryWhere, ReceivingAddressQuery>> {

        public QueryWhere(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, ReceivingAddressUpdate, ReceivingAddressQuery>
        implements ISegment<WhereApply<UpdateWhere, ReceivingAddressQuery>> {

        public UpdateWhere(ReceivingAddressUpdate update) {
            super(update);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, ReceivingAddressQuery>
        implements ISegment<GroupBy> {

        public GroupBy(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, ReceivingAddressQuery>
        implements ISegment<HavingApply<Having, ReceivingAddressQuery>> {

        public Having(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class OrderBy extends OrderByBase<OrderBy, ReceivingAddressQuery>
        implements ISegment<OrderBy> {

        public OrderBy(ReceivingAddressQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, ReceivingAddressUpdate>
        implements ISegment<UpdateApply<UpdateSetter, ReceivingAddressUpdate>> {

        public UpdateSetter(ReceivingAddressUpdate update) {
            super(update);
        }
    }
}