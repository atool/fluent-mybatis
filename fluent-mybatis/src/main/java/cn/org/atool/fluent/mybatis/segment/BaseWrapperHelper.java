package cn.org.atool.fluent.mybatis.segment;

import static cn.org.atool.fluent.mybatis.If.isBlank;

public class BaseWrapperHelper {
    /**
     * BaseWrapper里面的方法是protected的, 这里做个桥接
     *
     * @param column
     * @param wrapper
     * @return
     */
    public static String appendAlias(String column, BaseWrapper wrapper) {
        return wrapper.appendAlias(column);
    }

    /**
     * 判断wrapper属性alias是否未设置
     *
     * @param wrapper
     * @return
     */
    public static boolean isBlankAlias(BaseWrapper wrapper) {
        return isBlank(wrapper.alias);
    }

    public static boolean isColumnName(String word) {
        if (isBlank(word)) {
            return false;
        }
        String _word = word.trim();
        if (word.matches("[`'\\\"].*") && word.matches(".*[`'\\\"]")) {
            _word = word.substring(1, word.length() - 1);
        }
        return !_word.matches("\\d+") && _word.matches("[\\d\\w_\\-]+");
    }
}