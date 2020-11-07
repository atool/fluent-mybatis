package cn.org.atool.fluent.mybatis.mapper;

/**
 * SQL like 枚举
 *
 * @author darui.wu
 */
public class SqlLike {
    /**
     * %值
     */

    public static String left(Object input) {
        return "%" + input;
    }

    /**
     * 值%
     */
    public static String right(Object input) {
        return input + "%";
    }

    /**
     * %值%
     */
    public static String like(Object input) {
        return "%" + input + "%";
    }
}