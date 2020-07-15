package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.demo.generate.helper.UserMapping;
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
public class UserWrapperHelper {
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

        protected Selector aggregateSelector(IAggregate aggregate) {
            return new Selector(this, aggregate);
        }

        /**
         * 别名
         **/

        public Selector id(String alias) {
            return this.set(UserMapping.id);
        }

        public Selector gmtCreated(String alias) {
            return this.set(UserMapping.gmtCreated);
        }

        public Selector gmtModified(String alias) {
            return this.set(UserMapping.gmtModified);
        }

        public Selector isDeleted(String alias) {
            return this.set(UserMapping.isDeleted);
        }

        public Selector addressId(String alias) {
            return this.set(UserMapping.addressId);
        }

        public Selector age(String alias) {
            this.currField = UserMapping.age;
            return this.applyAs(this.aggregate, alias);
        }

        public Selector grade(String alias) {
            return this.set(UserMapping.grade);
        }

        public Selector userName(String alias) {
            return this.set(UserMapping.userName);
        }

        public Selector version(String alias) {
            return this.set(UserMapping.version);
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
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, UserUpdate, UserQuery>
        implements ISegment<WhereApply<UpdateWhere, UserQuery>> {

        public UpdateWhere(UserUpdate update) {
            super(update);
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
        implements ISegment<HavingApply<Having, UserQuery>> {

        public Having(UserQuery query) {
            super(query);
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