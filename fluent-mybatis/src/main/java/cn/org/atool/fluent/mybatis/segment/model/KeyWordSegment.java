package cn.org.atool.fluent.mybatis.segment.model;

import cn.org.atool.fluent.mybatis.interfaces.ISqlSegment;
import lombok.Getter;

import static cn.org.atool.fluent.mybatis.segment.model.StrConstant.SPACE;

/**
 * KeyWordSegment
 *
 * @author darui.wu
 * @create 2020/6/20 8:35 下午
 */
public enum KeyWordSegment implements ISqlSegment {

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
    AND("AND", true),
    /**
     * or
     */
    OR("OR", true);

    @Getter
    private String sqlSegment;

    @Getter
    private boolean isAndOr;

    KeyWordSegment(String keyWord) {
        this(keyWord, false);
    }

    KeyWordSegment(String keyWord, boolean isAndOr) {
        this.sqlSegment = SPACE + keyWord + SPACE;
        this.isAndOr = isAndOr;
    }
}