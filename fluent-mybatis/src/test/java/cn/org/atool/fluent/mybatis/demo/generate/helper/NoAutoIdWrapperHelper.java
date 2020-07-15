package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.demo.generate.helper.NoAutoIdMapping;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.NoAutoIdQuery;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.NoAutoIdUpdate;

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
        R set(FieldMapping fieldMapping);

        default R id() {
            return this.set(NoAutoIdMapping.id);
        }

        default R column1() {
            return this.set(NoAutoIdMapping.column1);
        }
    }

    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, NoAutoIdQuery>
        implements ISegment<Selector> {

        public Selector(NoAutoIdQuery query) {
            super(query);
        }

        protected Selector(Selector selector, IAggregate aggregate) {
            super(selector, aggregate);
        }

        protected Selector aggregateSelector(IAggregate aggregate) {
            return new Selector(this, aggregate);
        }
        /** 别名 **/

        public Selector id(String alias) {
            return this.set(NoAutoIdMapping.id);
        }

        public Selector column1(String alias) {
            return this.set(NoAutoIdMapping.column1);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, NoAutoIdQuery, NoAutoIdQuery>
        implements ISegment<WhereApply<QueryWhere, NoAutoIdQuery>> {

        public QueryWhere(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, NoAutoIdUpdate, NoAutoIdQuery>
        implements ISegment<WhereApply<UpdateWhere, NoAutoIdQuery>> {

        public UpdateWhere(NoAutoIdUpdate update) {
            super(update);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, NoAutoIdQuery>
        implements ISegment<GroupBy> {

        public GroupBy(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, NoAutoIdQuery>
        implements ISegment<HavingApply<Having, NoAutoIdQuery>> {

        public Having(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class QueryOrderBy extends OrderByBase<QueryOrderBy, NoAutoIdQuery>
        implements ISegment<OrderByApply<QueryOrderBy, NoAutoIdQuery>> {

        public QueryOrderBy(NoAutoIdQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, NoAutoIdUpdate>
        implements ISegment<OrderByApply<UpdateOrderBy, NoAutoIdUpdate>> {

        public UpdateOrderBy(NoAutoIdUpdate updator) {
            super(updator);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, NoAutoIdUpdate>
        implements ISegment<UpdateApply<UpdateSetter, NoAutoIdUpdate>> {

        public UpdateSetter(NoAutoIdUpdate update) {
            super(update);
        }
    }
}