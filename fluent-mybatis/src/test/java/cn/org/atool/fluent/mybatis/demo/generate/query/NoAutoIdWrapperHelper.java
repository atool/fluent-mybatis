package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.annotation.FieldMeta;
import cn.org.atool.fluent.mybatis.condition.base.*;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoAutoIdMP;

/**
 * <p>
 * NoAutoIdWrapperHelper
 * NoAutoIdEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class NoAutoIdWrapperHelper {
    public interface ISegment<R> {
        R set(FieldMeta fieldMeta);

        default R id() {
            return this.set(NoAutoIdMP.id);
        }

        default R column1() {
            return this.set(NoAutoIdMP.column1);
        }
    }
    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, NoAutoIdQuery>
        implements ISegment<SelectorApply<Selector, NoAutoIdQuery>> {

        Selector(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, NoAutoIdQuery, NoAutoIdQuery>
        implements ISegment<WhereApply<QueryWhere, NoAutoIdQuery>> {

        QueryWhere(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, NoAutoIdUpdate, NoAutoIdQuery>
        implements ISegment<WhereApply<UpdateWhere, NoAutoIdQuery>> {

        UpdateWhere(NoAutoIdUpdate update) {
            super(update);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, NoAutoIdQuery>
        implements ISegment<GroupBy> {

        GroupBy(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, NoAutoIdQuery>
        implements ISegment<HavingApply<Having, NoAutoIdQuery>> {

        Having(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class OrderBy extends OrderByBase<OrderBy, NoAutoIdQuery>
        implements ISegment<OrderBy> {

        OrderBy(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, NoAutoIdUpdate>
        implements ISegment<UpdateApply<UpdateSetter, NoAutoIdUpdate>> {

        UpdateSetter(NoAutoIdUpdate update) {
            super(update);
        }
    }
}