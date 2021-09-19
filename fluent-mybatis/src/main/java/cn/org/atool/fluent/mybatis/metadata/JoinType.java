package cn.org.atool.fluent.mybatis.metadata;

import cn.org.atool.fluent.mybatis.segment.fragment.CachedFrag;

/**
 * 关联查询类型
 */
public enum JoinType {
    /**
     * inner join
     */
    Join("JOIN"),
    /**
     * left join
     */
    LeftJoin("LEFT JOIN"),
    /**
     * right join
     */
    RightJoin("RIGHT JOIN");

    private final CachedFrag join;

    JoinType(String join) {
        this.join = CachedFrag.set(join);
    }

    public CachedFrag join() {
        return this.join;
    }
}