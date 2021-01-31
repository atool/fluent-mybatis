package cn.org.atool.fluent.mybatis;

import cn.org.atool.fluent.mybatis.ifs.Ifs;
import cn.org.atool.fluent.mybatis.ifs.InIfs;

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
     * 校验集合是否为空
     *
     * @param coll 入参
     * @return boolean
     */
    static boolean isEmpty(Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 校验集合是否不为空
     *
     * @param coll 入参
     * @return boolean
     */
    static boolean notEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断Map是否为空
     *
     * @param map 入参
     * @return boolean
     */
    static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断Map是否不为空
     *
     * @param map 入参
     * @return boolean
     */
    static boolean notEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断数据是否为空
     *
     * @param array 长度
     * @return 数组对象为null或者长度为 0 时，返回 false
     */
    static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否不为空
     *
     * @param array 数组
     * @return 数组对象内含有任意对象时返回 true
     * @see If#isEmpty(Object[])
     */
    static boolean notEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断数据是否为空
     *
     * @param array 长度
     * @return 数组对象为null或者长度为 0 时，返回 false
     */
    static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否不为空
     *
     * @param array 数组
     * @return 数组对象内含有任意对象时返回 true
     * @see If#isEmpty(Object[])
     */
    static boolean notEmpty(int[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断数据是否为空
     *
     * @param array 长度
     * @return 数组对象为null或者长度为 0 时，返回 false
     */
    static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }


    /**
     * 判断数组是否不为空
     *
     * @param array 数组
     * @return 数组对象内含有任意对象时返回 true
     * @see If#isEmpty(Object[])
     */
    static boolean notEmpty(long[] array) {
        return !isEmpty(array);
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
