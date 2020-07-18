package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.UserUpdate;

/**
 * <p>
 * UserWrapperHelper
 * UserEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class UserWrapperHelper implements UserMapping {
    public interface ISegment<R> {
        R set(FieldMapping fieldMapping);

        default R id() {
            return this.set(UserMapping.id);
        }

        default R gmtCreated() {
            return this.set(UserMapping.gmtCreated);
        }

        default R gmtModified() {
            return this.set(UserMapping.gmtModified);
        }

        default R isDeleted() {
            return this.set(UserMapping.isDeleted);
        }

        default R addressId() {
            return this.set(UserMapping.addressId);
        }

        default R age() {
            return this.set(UserMapping.age);
        }

        default R grade() {
            return this.set(UserMapping.grade);
        }

        default R userName() {
            return this.set(UserMapping.userName);
        }

        default R version() {
            return this.set(UserMapping.version);
        }
    }

    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, UserQuery>
        implements ISegment<Selector> {

        public Selector(UserQuery query) {
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

        public Selector addressId(String alias) {
            return this.process(addressId, alias);
        }

        public Selector age(String alias) {
            return this.process(age, alias);
        }

        public Selector grade(String alias) {
            return this.process(grade, alias);
        }

        public Selector userName(String alias) {
            return this.process(userName, alias);
        }

        public Selector version(String alias) {
            return this.process(version, alias);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, UserQuery, UserQuery>
        implements ISegment<WhereApply<QueryWhere, UserQuery>> {

        public QueryWhere(UserQuery query) {
            super(query);
        }

        private QueryWhere(UserQuery query, QueryWhere where) {
            super(query, where);
        }

        @Override
        protected QueryWhere buildOr(QueryWhere and) {
            return new QueryWhere((UserQuery) this.wrapper, and);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, UserUpdate, UserQuery>
        implements ISegment<WhereApply<UpdateWhere, UserQuery>> {

        public UpdateWhere(UserUpdate update) {
            super(update);
        }

        private UpdateWhere(UserUpdate update, UpdateWhere where) {
            super(update, where);
        }

        @Override
        protected UpdateWhere buildOr(UpdateWhere and) {
            return new UpdateWhere((UserUpdate) this.wrapper, and);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, UserQuery>
        implements ISegment<GroupBy> {

        public GroupBy(UserQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, UserQuery>
        implements ISegment<HavingOperator<Having>> {

        public Having(UserQuery query) {
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
    public static final class QueryOrderBy extends OrderByBase<QueryOrderBy, UserQuery>
        implements ISegment<OrderByApply<QueryOrderBy, UserQuery>> {

        public QueryOrderBy(UserQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, UserUpdate>
        implements ISegment<OrderByApply<UpdateOrderBy, UserUpdate>> {

        public UpdateOrderBy(UserUpdate updator) {
            super(updator);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, UserUpdate>
        implements ISegment<UpdateApply<UpdateSetter, UserUpdate>> {

        public UpdateSetter(UserUpdate update) {
            super(update);
        }
    }
}