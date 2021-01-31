package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.ifs.Ifs;
import cn.org.atool.fluent.mybatis.ifs.InIfs;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * 常见断言
 *
 * @author wudarui
 */
public interface If {
    /**
     * 总是真的
     *
     * @param o
     * @return
     */
    static boolean everTrue(Object o) {
        return true;
    }

    /**
     * 总是假的
     *
     * @param o
     * @return
     */
    static boolean everFalse(Object o) {
        return false;
    }

    /**
     * 判断数据对象(Collection, Map, Array)是否不为空
     *
     * @param value Collection, Map, Array
     * @return 数组对象内含有任意对象时返回 true
     * @see If#isEmpty(Object)
     */
    static boolean notEmpty(Object value) {
        return !isEmpty(value);
    }

    /**
     * 判断数据对象(Collection, Map, Array)是否为空
     *
     * @param value Collection, Map, Array
     * @return
     */
    static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        } else if (value instanceof Collection) {
            return ((Collection) value).isEmpty();
        } else if (value instanceof Map) {
            return ((Map) value).isEmpty();
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) == 0;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param cs 需要判断字符串
     * @return 判断结果
     */
    static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param cs 需要判断字符串
     * @return 判断结果
     */
    static boolean notBlank(final CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 判断对象是否为空
     *
     * @param object ignore
     * @return ignore
     */
    static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * 判断对象是否非空
     *
     * @param object ignore
     * @return ignore
     */
    static boolean notNull(Object object) {
        return !isNull(object);
    }

    /**
     * 多条件选择
     *
     * @return
     */
    static <T> Ifs<T> test() {
        return new Ifs<>();
    }

    /**
     * 多条件选择
     *
     * @return
     */
    static <T> InIfs<T> testIn() {
        return new InIfs<T>();
    }
}
