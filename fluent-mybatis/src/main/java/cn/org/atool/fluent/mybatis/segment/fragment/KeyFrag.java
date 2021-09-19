package cn.org.atool.fluent.mybatis.segment.fragment;

import cn.org.atool.fluent.mybatis.metadata.DbType;

import static cn.org.atool.fluent.mybatis.mapper.StrConstant.SPACE;

/**
 * KeyWordSegment
 *
 * @author darui.wu 2020/6/20 8:35 下午
 */
public enum KeyFrag implements IFragment {
    /**
     * SELECT
     */
    SELECT("SELECT"),
    /**
     * FROM
     */
    FROM("FROM"),
    /**
     * WHERE
     */
    WHERE("WHERE"),
    /**
     * GROUP BY标识
     */
    GROUP_BY("GROUP BY"),
    /**
     * HAVING标识
     */
    HAVING("HAVING"),
    /**
     * ORDER BY标识
     */
    ORDER_BY("ORDER BY"),
    /**
     * and
     */
    AND("AND"),
    /**
     * or
     */
    OR("OR");

    /**
     * 代码片段
     */
    private final String keyWord;

    KeyFrag(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public String get(DbType db) {
        return this.keyWord + SPACE;
    }

    @Override
    public String toString() {
        return this.keyWord;
    }

    public String key() {
        return this.keyWord;
    }
}