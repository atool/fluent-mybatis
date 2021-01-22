package cn.org.atool.fluent.mybatis.base.splice;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;

/**
 * FreeWrapperHelper
 *
 * @author darui.wu
 */
final class FreeWrapperHelper {
    public interface ISegment<R> {
        R set(FieldMapping fieldMapping);
    }

    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, FreeQuery> implements ISegment<Selector> {
        public Selector(FreeQuery query) {
            super(query);
        }

        protected Selector(Selector selector, IAggregate aggregate) {
            super(selector, aggregate);
        }

        @Override
        protected Selector aggregateSegment(IAggregate aggregate) {
            return new Selector(this, aggregate);
        }

        public Selector avg(String column, String alias) {
            String _column = BaseWrapperHelper.appendAlias(column, super.wrapper);
            this.applyAs(String.format("AVG(%s)", _column), alias);
            return this;
        }

        public Selector sum(String column, String alias) {
            String _column = BaseWrapperHelper.appendAlias(column, super.wrapper);
            this.applyAs(String.format("SUM(%s)", _column), alias);
            return this;
        }

        public Selector max(String column, String alias) {
            String _column = BaseWrapperHelper.appendAlias(column, super.wrapper);
            this.applyAs(String.format("MAX(%s)", _column), alias);
            return this;
        }

        public Selector min(String column, String alias) {
            String _column = BaseWrapperHelper.appendAlias(column, super.wrapper);
            this.applyAs(String.format("MIN(%s)", _column), alias);
            return this;
        }
    }

    /**
     * query where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, FreeQuery, FreeQuery> {
        public QueryWhere(FreeQuery query) {
            super(query);
        }

        private QueryWhere(FreeQuery query, QueryWhere where) {
            super(query, where);
        }

        @Override
        protected QueryWhere buildOr(QueryWhere and) {
            return new QueryWhere((FreeQuery) this.wrapper, and);
        }
    }

    /**
     * update where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, FreeUpdate, FreeQuery> {
        public UpdateWhere(FreeUpdate updater) {
            super(updater);
        }

        private UpdateWhere(FreeUpdate updater, UpdateWhere where) {
            super(updater, where);
        }

        @Override
        protected UpdateWhere buildOr(UpdateWhere and) {
            return new UpdateWhere((FreeUpdate) this.wrapper, and);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, FreeQuery> implements ISegment<GroupBy> {
        public GroupBy(FreeQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, FreeQuery> implements ISegment<HavingOperator<Having>> {
        public Having(FreeQuery query) {
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
     * Query OrderBy设置
     */
    public static final class QueryOrderBy extends OrderByBase<QueryOrderBy, FreeQuery> implements ISegment<OrderByApply<QueryOrderBy, FreeQuery>> {
        public QueryOrderBy(FreeQuery query) {
            super(query);
        }
    }

    /**
     * Update OrderBy设置
     */
    public static final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, FreeUpdate> implements ISegment<OrderByApply<UpdateOrderBy, FreeUpdate>> {
        public UpdateOrderBy(FreeUpdate updater) {
            super(updater);
        }
    }

    /**
     * Update set 设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, FreeUpdate> implements ISegment<UpdateApply<UpdateSetter, FreeUpdate>> {
        public UpdateSetter(FreeUpdate updater) {
            super(updater);
        }
    }
}