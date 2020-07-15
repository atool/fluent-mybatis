package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
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
        R set(FieldMapping fieldMapping);

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
        implements ISegment<Selector> {

        public Selector(AddressQuery query) {
            super(query);
        }

        protected Selector(Selector selector, IAggregate aggregate) {
            super(selector, aggregate);
        }

        @Override
        protected Selector aggregateSelector(IAggregate aggregate) {
            return new Selector(this, aggregate);
        }
        /** 别名 **/

        public Selector id(String alias) {
            this.currField = AddressMapping.id;
            return this.applyAs(aggregate, alias);
        }

        public Selector gmtCreated(String alias) {
            this.currField = AddressMapping.gmtCreated;
            return this.applyAs(aggregate, alias);
        }

        public Selector gmtModified(String alias) {
            this.currField = AddressMapping.gmtModified;
            return this.applyAs(aggregate, alias);
        }

        public Selector isDeleted(String alias) {
            this.currField = AddressMapping.isDeleted;
            return this.applyAs(aggregate, alias);
        }

        public Selector address(String alias) {
            this.currField = AddressMapping.address;
            return this.applyAs(aggregate, alias);
        }

        public Selector userId(String alias) {
            this.currField = AddressMapping.userId;
            return this.applyAs(aggregate, alias);
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
        implements ISegment<OrderByApply<QueryOrderBy, AddressQuery>> {

        public QueryOrderBy(AddressQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, AddressUpdate>
        implements ISegment<OrderByApply<UpdateOrderBy, AddressUpdate>> {

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