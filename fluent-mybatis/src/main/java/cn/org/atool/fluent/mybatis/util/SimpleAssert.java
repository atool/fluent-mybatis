package cn.org.atool.fluent.mybatis.util;

import cn.org.atool.fluent.mybatis.exception.NullParameterException;
import com.mybatisplus.core.toolkit.StringUtils;

import java.util.Collection;

/**
 * 简单断言
 *
 * @author wudarui
 */
public class SimpleAssert {
    /**
     * 断言对象不能为null
     *
     * @param property
     * @param value
     */
    public static void assertNotNull(String property, Object value) {
        if (value == null) {
            throw new NullParameterException("the parameter[" + property + "] can't be null.");
        }
    }

    /**
     * 断言对象不能为null
     *
     * @param property
     * @param value1
     * @param value2
     * @param <T>
     */
    public static <T> void assertNotNull(String property, T value1, T value2) {
        if (value1 == null || value2 == null) {
            throw new NullParameterException("the parameter[" + property + "] can't be null.");
        }
    }

    /**
     * 断言字符串不能为空
     *
     * @param property
     * @param value
     */
    public static void assertNotBlank(String property, String value) {
        if (StringUtils.isEmpty(value)) {
            throw new NullParameterException("the parameter[" + property + "] can't be blank.");
        }
    }

    /**
     * 断言list参数不能为空
     *
     * @param property
     * @param list
     */
    public static void assertNotEmpty(String property, Collection list) {
        if (list == null || list.size() == 0) {
            throw new NullParameterException("the parameter[" + property + "] can't be empty.");
        }
    }

    /**
     * 断言数组array参数不能为空
     *
     * @param property
     * @param array
     */
    public static void assertNotEmpty(String property, Object[] array) {
        if (array == null || array.length == 0) {
            throw new NullParameterException("the parameter[" + property + "] can't be empty.");
        }
    }
}
