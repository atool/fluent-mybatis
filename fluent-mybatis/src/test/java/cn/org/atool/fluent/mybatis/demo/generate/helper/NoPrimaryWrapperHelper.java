package cn.org.atool.fluent.mybatis.demo.generate.helper;

import cn.org.atool.fluent.mybatis.base.model.FieldMapping;
import cn.org.atool.fluent.mybatis.functions.IAggregate;
import cn.org.atool.fluent.mybatis.segment.*;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.NoPrimaryQuery;
import cn.org.atool.fluent.mybatis.demo.generate.wrapper.NoPrimaryUpdate;

/**
 * <p>
 * NoPrimaryWrapperHelper
 * NoPrimaryEntity 查询更新帮助类
 * </p>
 *
 * @author generate code
 */
public class NoPrimaryWrapperHelper implements NoPrimaryMapping {
    public interface ISegment<R> {
        R set(FieldMapping fieldMapping);

        default R column1() {
            return this.set(NoPrimaryMapping.column1);
        }

        default R column2() {
            return this.set(NoPrimaryMapping.column2);
        }
    }

    /**
     * select字段设置
     */
    public static final class Selector extends SelectorBase<Selector, NoPrimaryQuery>
        implements ISegment<Selector> {

        public Selector(NoPrimaryQuery query) {
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

        public Selector column1(String alias) {
            return this.process(column1, alias);
        }

        public Selector column2(String alias) {
            return this.process(column2, alias);
        }
    }

    /**
     * where条件设置
     */
    public static class QueryWhere extends WhereBase<QueryWhere, NoPrimaryQuery, NoPrimaryQuery>
        implements ISegment<WhereApply<QueryWhere, NoPrimaryQuery>> {

        public QueryWhere(NoPrimaryQuery query) {
            super(query);
        }

        private QueryWhere(NoPrimaryQuery query, QueryWhere where) {
            super(query, where);
        }

        @Override
        protected QueryWhere buildOr(QueryWhere and) {
            return new QueryWhere((NoPrimaryQuery) this.wrapper, and);
        }
    }

    /**
     * where条件设置
     */
    public static class UpdateWhere extends WhereBase<UpdateWhere, NoPrimaryUpdate, NoPrimaryQuery>
        implements ISegment<WhereApply<UpdateWhere, NoPrimaryQuery>> {

        public UpdateWhere(NoPrimaryUpdate update) {
            super(update);
        }

        private UpdateWhere(NoPrimaryUpdate update, UpdateWhere where) {
            super(update, where);
        }

        @Override
        protected UpdateWhere buildOr(UpdateWhere and) {
            return new UpdateWhere((NoPrimaryUpdate) this.wrapper, and);
        }
    }

    /**
     * 分组设置
     */
    public static final class GroupBy extends GroupByBase<GroupBy, NoPrimaryQuery>
        implements ISegment<GroupBy> {

        public GroupBy(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * 分组Having条件设置
     */
    public static final class Having extends HavingBase<Having, NoPrimaryQuery>
        implements ISegment<HavingOperator<Having>> {

        public Having(NoPrimaryQuery query) {
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
    public static final class QueryOrderBy extends OrderByBase<QueryOrderBy, NoPrimaryQuery>
        implements ISegment<OrderByApply<QueryOrderBy, NoPrimaryQuery>> {

        public QueryOrderBy(NoPrimaryQuery query) {
            super(query);
        }
    }

    /**
     * OrderBy设置
     */
    public static final class UpdateOrderBy extends OrderByBase<UpdateOrderBy, NoPrimaryUpdate>
        implements ISegment<OrderByApply<UpdateOrderBy, NoPrimaryUpdate>> {

        public UpdateOrderBy(NoPrimaryUpdate updator) {
            super(updator);
        }
    }

    /**
     * 字段更新设置
     */
    public static final class UpdateSetter extends UpdateBase<UpdateSetter, NoPrimaryUpdate>
        implements ISegment<UpdateApply<UpdateSetter, NoPrimaryUpdate>> {

        public UpdateSetter(NoPrimaryUpdate update) {
            super(update);
        }
    }
}