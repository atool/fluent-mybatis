package cn.org.atool.fluent.mybatis.demo.generate.query;

import cn.org.atool.fluent.mybatis.base.model.FieldMeta;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.demo.generate.mapping.NoPrimaryMP;

/**
 * <p>
 * NoPrimaryWrapperHelper
 * NoPrimaryEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class NoPrimaryWrapperHelper {
    public interface ISegment<R> {
        R set(FieldMeta fieldMeta);

        default R column1() {
            return this.set(NoPrimaryMP.column1);
        }

        default R column2() {
            return this.set(NoPrimaryMP.column2);
        }
    }
    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, NoPrimaryQuery>
        implements ISegment<SelectorApply<Selector, NoPrimaryQuery>> {

        Selector(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, NoPrimaryQuery, NoPrimaryQuery>
        implements ISegment<WhereApply<QueryWhere, NoPrimaryQuery>> {

        QueryWhere(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, NoPrimaryUpdate, NoPrimaryQuery>
        implements ISegment<WhereApply<UpdateWhere, NoPrimaryQuery>> {

        UpdateWhere(NoPrimaryUpdate update) {
            super(update);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, NoPrimaryQuery>
        implements ISegment<GroupBy> {

        GroupBy(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, NoPrimaryQuery>
        implements ISegment<HavingApply<Having, NoPrimaryQuery>> {

        Having(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class OrderBy extends OrderByBase<OrderBy, NoPrimaryQuery>
        implements ISegment<OrderBy> {

        OrderBy(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, NoPrimaryUpdate>
        implements ISegment<UpdateApply<UpdateSetter, NoPrimaryUpdate>> {

        UpdateSetter(NoPrimaryUpdate update) {
            super(update);
        }
    }
}