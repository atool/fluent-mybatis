package cn.org.atool.fluent.mybatis.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.generate.wrapper.NoAutoIdQuery;
import cn.org.atool.fluent.mybatis.generate.wrapper.NoAutoIdUpdate;
import cn.org.atool.fluent.mybatis.segment.*;

/**
 * <p>
 * NoAutoIdWrapperHelper
 * NoAutoIdEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class NoAutoIdWrapperHelper implements NoAutoIdMapping {
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

        @Override
        protected Selector aggregateSegment(IAggregate aggregate) {
            return new Selector(this, aggregate);
        }
        /** 别名 **/

        public Selector id(String alias) {
            return this.process(id, alias);
        }

        public Selector column1(String alias) {
            return this.process(column1, alias);
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

        private QueryWhere(NoAutoIdQuery query, QueryWhere where) {
            super(query, where);
        }

        @Override
        protected QueryWhere buildOr(QueryWhere and) {
            return new QueryWhere((NoAutoIdQuery) this.wrapper, and);
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

        private UpdateWhere(NoAutoIdUpdate update, UpdateWhere where) {
            super(update, where);
        }

        @Override
        protected UpdateWhere buildOr(UpdateWhere and) {
            return new UpdateWhere((NoAutoIdUpdate) this.wrapper, and);
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
        implements ISegment<HavingOperator<Having>> {

        public Having(NoAutoIdQuery query) {
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