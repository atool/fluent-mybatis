package cn.org.atool.fluent.mybatis.segment;

import cn.org.atool.fluent.mybatis.base.crud.IQuery;
import cn.org.atool.fluent.mybatis.base.crud.IUpdate;
import cn.org.atool.fluent.mybatis.base.crud.IWrapper;
import cn.org.atool.fluent.mybatis.segment.model.PagedOffset;

public class LimitSegment<W extends IWrapper> {
    protected final W wrapper;

    public LimitSegment(W wrapper) {
        this.wrapper = wrapper;
    }

    /**
     * 设置limit值
     *
     * @param limit 最大查询数量
     * @return self
     */
    public W by(int limit) {
        this.wrapper.getWrapperData().setPaged(new PagedOffset(0, limit));
        return wrapper;
    }

    /**
     * 更新限制
     *
     * @param <Q>
     */
    public static class UpdateLimit<Q extends IUpdate> extends LimitSegment<Q> {
        public UpdateLimit(Q wrapper) {
            super(wrapper);
        }
    }

    /**
     * 查询限制
     *
     * @param <Q>
     */
    public static class QueryLimit<Q extends IQuery> extends LimitSegment<Q> {
        public QueryLimit(Q wrapper) {
            super(wrapper);
        }

        /**
         * 设置limit值
         *
         * @param start 开始查询偏移量
         * @param limit 最大查询数量
         * @return self
         */
        public Q by(int start, int limit) {
            this.wrapper.getWrapperData().setPaged(new PagedOffset(start, limit));
            return wrapper;
        }
    }
}
