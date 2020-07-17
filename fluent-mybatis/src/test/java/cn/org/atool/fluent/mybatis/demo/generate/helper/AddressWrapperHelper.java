package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;
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
public class AddressWrapperHelper implements AddressMapping {
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
        protected Selector aggregateSegment(IAggregate aggregate) {
            return new Selector(this, aggregate);
        }
        /** 别名 **/

        public Selector id(String alias) {
            return this.process(id, alias);
        }

        public Selector gmtCreated(String alias) {
            return this.process(gmtCreated, alias);
        }

        public Selector gmtModified(String alias) {
            return this.process(gmtModified, alias);
        }

        public Selector isDeleted(String alias) {
            return this.process(isDeleted, alias);
        }

        public Selector address(String alias) {
            return this.process(address, alias);
        }

        public Selector userId(String alias) {
            return this.process(userId, alias);
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

        private QueryWhere(AddressQuery query, QueryWhere and) {
            super(query, and);
        }

        @Override
        protected QueryWhere orWhere(QueryWhere and) {
            return new QueryWhere((AddressQuery) this.wrapper, and);
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

        private UpdateWhere(AddressUpdate update, UpdateWhere and) {
            super(update, and);
        }

        @Override
        protected UpdateWhere orWhere(UpdateWhere and) {
            return new UpdateWhere((AddressUpdate) this.wrapper, and);
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
        implements ISegment<HavingOperator<Having>> {

        public Having(AddressQuery query) {
            super(query);
        }

        protected Having(Having having, IAggregate aggregate) {
            super(having, aggregate);
        }

        @Override
        protected Having aggregateSegment(IAggregate aggregate) {
            return new Having(this, aggregate);
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