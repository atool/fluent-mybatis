package cn.org.atool.fluent.mybatis.base.free;

import cn.org.atool.fluent.mybatis.segment.BaseWrapper;

public interface FreeKit {
    /**
     * 桥接方法
     *
     * @param query BaseWrapper
     * @return FreeQuery
     */
    static FreeQuery newFreeQuery(BaseWrapper query) {
        return new FreeQuery(query.table(false), query.getTableAlias());
    }
}
