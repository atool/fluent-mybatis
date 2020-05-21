package cn.org.atool.fluent.mybatis.condition;


import cn.org.atool.fluent.mybatis.condition.interfaces.ISqlSegment;
import cn.org.atool.fluent.mybatis.util.Constants;

/**
 * SQL 保留关键字枚举
 *
 * @author darui.wu
 */
public enum SqlKeyword implements ISqlSegment {
    /**
     * and
     */
    AND("AND"),
    /**
     * or
     */
    OR("OR"),
    IN("IN"),
    NOT("NOT"),
    LIKE("LIKE"),
    EQ(Constants.EQUALS),
    NE("<>"),
    GT(Constants.RIGHT_CHEV),
    GE(">="),
    LT(Constants.LEFT_CHEV),
    LE("<="),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    GROUP_BY("GROUP BY"),
    HAVING("HAVING"),
    ORDER_BY("ORDER BY"),
    EXISTS("EXISTS"),
    BETWEEN("BETWEEN"),
    ASC("ASC"),
    DESC("DESC");

    private final String keyword;

    SqlKeyword(final String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String getSqlSegment() {
        return this.keyword;
    }
}