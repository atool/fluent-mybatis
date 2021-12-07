package cn.org.atool.fluent.mybatis.typehandler;

/**
 * 类型转换
 *
 * @param <T> 目标对象类型
 * @author darui.wu
 */
public interface IConvertor<T> {
    /**
     * 转换处理
     *
     * @param value 输入值
     * @return 转换后的输出值
     */
    T get(Object value);

    /**
     * 当前值是否符合转换要求
     *
     * @param value 输入值
     * @return true/false
     */
    @SuppressWarnings("unused")
    default boolean match(Object value) {
        return true;
    }
}