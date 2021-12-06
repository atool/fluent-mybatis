package cn.org.atool.fluent.form.kits;

import static cn.org.atool.fluent.form.kits.MethodArgNamesKit.matchWord;

/**
 * JPA style 方法枚举
 *
 * @author darui.wu
 */
public enum MethodStyle {
    FindBy("findBy", 6),
    CountBy("countBy", 7),
    ExistsBy("existsBy", 8),
    TopNBy("top\\d+By", 0);

    private final String name;

    public final int offset;


    MethodStyle(String name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    /**
     * 是否 findBy 方法
     */
    public static boolean isFindBy(String method) {
        return method.startsWith(FindBy.name);
    }

    /**
     * 是否 countBy 方法
     */
    public static boolean isCountBy(String method) {
        return method.startsWith(CountBy.name);
    }

    /**
     * 是否 existsBy 方法
     */
    public static boolean isExistsBy(String method) {
        return method.startsWith(ExistsBy.name);
    }

    /**
     * 是否top N方法
     *
     * @param method 方法名称
     * @return null: 非topN方法; [0]=offset, [1]=N
     */
    public static int[] isTopNBy(String method) {
        if (!method.startsWith("top")) {
            return null;
        }
        StringBuilder digit = new StringBuilder();
        for (int index = 3; index < method.length(); index++) {
            char ch = method.charAt(index);
            if (ch >= '0' && ch <= '9') {
                digit.append(ch);
            } else if (matchWord(method, index, "By")) {
                String text = digit.toString();
                return new int[]{index + 2, text.isEmpty() ? 1 : Integer.parseInt(text)};
            } else {
                return null;
            }
        }
        return null;
    }
}