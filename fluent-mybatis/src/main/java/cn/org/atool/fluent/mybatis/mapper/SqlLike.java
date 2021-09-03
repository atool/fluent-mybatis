package cn.org.atool.fluent.mybatis.mapper;

import java.util.Objects;

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
        assertInput(input);
        return "%" + input;
    }

    /**
     * 值%SqlOp
     */
    public static String right(Object input) {
        assertInput(input);
        return input + "%";
    }

    /**
     * %值%
     */
    public static String like(Object input) {
        assertInput(input);
        return "%" + input + "%";
    }

    private static void assertInput(Object input) {
        String text = String.valueOf(input);
        if (Objects.equals("%", text) || Objects.equals("_", text)) {
            throw new IllegalArgumentException("The like operation cannot be string '%' or '_' only");
        }
    }
}