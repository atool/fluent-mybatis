package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.IWrapper;

/**
 * JoinQueryWhere
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface JoinSegment {

    class QueryWhere extends WhereBase {
        protected QueryWhere(JoinQuery wrapper) {
            super(wrapper);
        }

        protected QueryWhere(JoinQuery wrapper, QueryWhere and) {
            super(wrapper, and);
        }

        @Override
        protected QueryWhere buildOr(WhereBase and) {
            return new QueryWhere((JoinQuery) this.wrapper, (QueryWhere) and);
        }
    }

    class OrderBy extends OrderByBase {
        protected OrderBy(IWrapper wrapper) {
            super(wrapper);
        }
    }
}