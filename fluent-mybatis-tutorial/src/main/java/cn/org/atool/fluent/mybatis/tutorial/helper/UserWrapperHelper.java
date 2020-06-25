package cn.org.atool.fluent.mybatis.tutorial.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.tutorial.helper.UserMapping;
import cn.org.atool.fluent.mybatis.tutorial.wrapper.UserQuery;
import cn.org.atool.fluent.mybatis.tutorial.wrapper.UserUpdate;

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
        R set(FieldMeta fieldMeta);

        default R id() {
            return this.set(UserMapping.id);
        }

        default R gmtModified() {
            return this.set(UserMapping.gmtModified);
        }

        default R isDeleted() {
            return this.set(UserMapping.isDeleted);
        }

        default R account() {
            return this.set(UserMapping.account);
        }

        default R avatar() {
            return this.set(UserMapping.avatar);
        }

        default R birthday() {
            return this.set(UserMapping.birthday);
        }

        default R eMail() {
            return this.set(UserMapping.eMail);
        }

        default R gmtCreate() {
            return this.set(UserMapping.gmtCreate);
        }

        default R password() {
            return this.set(UserMapping.password);
        }

        default R phone() {
            return this.set(UserMapping.phone);
        }

        default R status() {
            return this.set(UserMapping.status);
        }

        default R userName() {
            return this.set(UserMapping.userName);
        }
    }
    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, UserQuery>
        implements ISegment<SelectorApply<Selector, UserQuery>> {

        public Selector(UserQuery query) {
            super(query);
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
    public static final class OrderBy extends OrderByBase<OrderBy, UserQuery>
        implements ISegment<OrderBy> {

        public OrderBy(UserQuery query) {
            super(query);
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