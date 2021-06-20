package cn.org.atool.fluent.mybatis.metadata;

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

    private final String join;

    JoinType(String join) {
        this.join = join;
    }

    public String join() {
        return this.join;
    }
}