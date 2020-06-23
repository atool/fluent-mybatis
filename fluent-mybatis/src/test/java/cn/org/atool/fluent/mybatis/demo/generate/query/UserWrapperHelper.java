package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.annotation.FieldMeta;
import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.UserMP;

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
            return this.set(UserMP.id);
        }

        default R gmtCreated() {
            return this.set(UserMP.gmtCreated);
        }

        default R gmtModified() {
            return this.set(UserMP.gmtModified);
        }

        default R isDeleted() {
            return this.set(UserMP.isDeleted);
        }

        default R addressId() {
            return this.set(UserMP.addressId);
        }

        default R age() {
            return this.set(UserMP.age);
        }

        default R userName() {
            return this.set(UserMP.userName);
        }

        default R version() {
            return this.set(UserMP.version);
        }
    }
    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, UserQuery>
        implements ISegment<SelectorApply<Selector, UserQuery>> {

        Selector(UserQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, UserQuery, UserQuery>
        implements ISegment<WhereApply<QueryWhere, UserQuery>> {

        QueryWhere(UserQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, UserUpdate, UserQuery>
        implements ISegment<WhereApply<UpdateWhere, UserQuery>> {

        UpdateWhere(UserUpdate update) {
            super(update);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, UserQuery>
        implements ISegment<GroupBy> {

        GroupBy(UserQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, UserQuery>
        implements ISegment<HavingApply<Having, UserQuery>> {

        Having(UserQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class OrderBy extends OrderByBase<OrderBy, UserQuery>
        implements ISegment<OrderBy> {

        OrderBy(UserQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, UserUpdate>
        implements ISegment<UpdateApply<UpdateSetter, UserUpdate>> {

        UpdateSetter(UserUpdate update) {
            super(update);
        }
    }
}