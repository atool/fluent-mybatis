package cn.org.atool.fluent.mybatis.mapper;

/**
 * SQL like 枚举
 *
 * @author darui.wu
 */
public class SqlLike {
    /**
     * 值%
     */

    public static String left(Object input) {
        assertInput(input);
        return input + "%";
    }

    /**
     * %值
     */
    public static String right(Object input) {
        assertInput(input);
        return "%" + input;
    }

    /**
     * %值%
     */
    public static String like(Object input) {
        assertInput(input);
        return "%" + input + "%";
    }

    /**
     * 验证输入值不仅仅是 '%' 组成
     *
     * @param input 输入值
     */
    private static void assertInput(Object input) {
        String text = String.valueOf(input);
        for (char ch : text.toCharArray()) {
            if (ch != '%') {
                return;
            }
        }
        throw new IllegalArgumentException("The like operation cannot be string '%' or empty only");
    }
}