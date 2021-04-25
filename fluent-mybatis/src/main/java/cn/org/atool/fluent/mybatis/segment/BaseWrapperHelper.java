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
        return isBlank(wrapper.tableAlias);
    }
}