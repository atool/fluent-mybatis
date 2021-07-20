package cn.org.atool.fluent.mybatis.segment;

import static cn.org.atool.fluent.mybatis.If.isBlank;

/**
 * BaseWrapperHelper
 *
 * @author wudarui
 */
@SuppressWarnings({"rawtypes"})
public class BaseWrapperHelper {
    /**
     * BaseWrapper里面的方法是protected的, 这里做个桥接
     *
     * @param column  column name
     * @param wrapper BaseWrapper
     * @return column with alias
     */
    public static String appendAlias(String column, BaseWrapper wrapper) {
        return wrapper.appendAlias(column);
    }

    /**
     * 判断wrapper属性alias是否未设置
     *
     * @param wrapper BaseWrapper
     * @return true:未定义别名
     */
    public static boolean isBlankAlias(BaseWrapper wrapper) {
        return isBlank(wrapper.tableAlias);
    }
}