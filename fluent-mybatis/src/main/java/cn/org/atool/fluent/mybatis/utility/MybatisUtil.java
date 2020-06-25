package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * MybatisUtil
 * 工具类
 *
 * @author darui.wu
 */
public class MybatisUtil {
    /**
     * 校验集合是否为空
     *
     * @param coll 入参
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     * 校验集合是否不为空
     *
     * @param coll 入参
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * 判断Map是否为空
     *
     * @param map 入参
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断Map是否不为空
     *
     * @param map 入参
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断数据是否为空
     *
     * @param array 长度
     * @return 数组对象为null或者长度为 0 时，返回 false
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组是否不为空
     *
     * @param array 数组
     * @return 数组对象内含有任意对象时返回 true
     * @see MybatisUtil#isEmpty(Object[])
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断字符串是否为空
     *
     * @param cs 需要判断字符串
     * @return 判断结果
     */
    public static boolean isEmpty(final CharSequence cs) {
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
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 判断对象是否为空
     *
     * @param object ignore
     * @return ignore
     */
    public static boolean isNotNull(Object object) {
        if (object instanceof CharSequence) {
            return isNotEmpty((CharSequence) object);
        } else {
            return object != null;
        }
    }

    /**
     * 安全的进行字符串 format
     *
     * @param target 目标字符串
     * @param params format 参数
     * @return format 后的
     */
    public static String format(String target, Object... params) {
        if (target.contains("%s") && MybatisUtil.isNotEmpty(params)) {
            return String.format(target, params);
        } else {
            return target;
        }
    }

    static Map<Character, String> Escape_Char = new HashMap<>();

    static {
        Escape_Char.put((char) 0, "\\0");
        Escape_Char.put('\n', "\\n");
        Escape_Char.put('\r', "\\r");
        Escape_Char.put('\\', "\\\\");
        Escape_Char.put('\'', "\\\'");
        Escape_Char.put('"', "\\\"");
        Escape_Char.put('\032', "\\Z");
    }

    /**
     * <p>
     * 获取该类的所有属性列表
     * </p>
     *
     * @param clazz 反射类
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        Class current = getProxyTargetClass(clazz);
        Map<String, Field> map = new HashMap<>();
        while (current.getSuperclass() != null && current != Object.class) {
            Field[] fields = current.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (!map.containsKey(fieldName) && !isStatic(field)) {
                    map.put(fieldName, field);
                }
            }
            current = current.getSuperclass();
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 过滤 static 和 transient 关键字修饰的属性
     *
     * @param field
     * @return
     */
    private static boolean isStatic(Field field) {
        return Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers());
    }

    /**
     * 代理 class 的名称
     */
    private static final List<String> PROXY_CLASS_NAMES = Arrays.asList(
        // cglib
        "net.sf.cglib.proxy.Factory",
        "org.springframework.cglib.proxy.Factory",
        // javassist
        "javassist.util.proxy.ProxyObject",
        "org.apache.ibatis.javassist.util.proxy.ProxyObject"
    );

    /**
     * <p>
     * 获取代理对象的target class
     * </p>
     *
     * @param clazz 传入
     * @return 如果是代理的class，返回父 class，否则返回自身
     */
    public static Class<?> getProxyTargetClass(Class<?> clazz) {
        if (clazz != null) {
            for (Class klass : clazz.getInterfaces()) {
                if (PROXY_CLASS_NAMES.contains(klass.getName())) {
                    return clazz.getSuperclass();
                }
            }
        }
        return clazz;
    }

    /**
     * 断言对象不能为null
     *
     * @param property
     * @param value
     */
    public static void assertNotNull(String property, Object value) {
        if (value == null) {
            throw FluentMybatisException.instance("the parameter[" + property + "] can't be null.", property);
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
            throw FluentMybatisException.instance("the parameter[%s] can't be null.", property);
        }
    }

    /**
     * 断言字符串不能为空
     *
     * @param property
     * @param value
     */
    public static void assertNotBlank(String property, String value) {
        if (MybatisUtil.isEmpty(value)) {
            throw FluentMybatisException.instance("the parameter[%s] can't be blank.", property);
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
            throw FluentMybatisException.instance("the parameter[%s] can't be empty.", property);
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
            throw FluentMybatisException.instance("the parameter[%s] can't be empty.", property);
        }
    }

    /**
     * 断言这个 boolean 为 true
     * <p>为 false 则抛出异常</p>
     *
     * @param expression boolean 值
     * @param message    消息
     */
    public static void isTrue(boolean expression, String message, Object... params) {
        if (!expression) {
            throw FluentMybatisException.instance(message, params);
        }
    }

    /**
     * 断言这个 object 不为 null
     * <p>为 null 则抛异常</p>
     *
     * @param object  对象
     * @param message 消息
     */
    public static void notNull(Object object, String message, Object... params) {
        isTrue(object != null, message, params);
    }

    /**
     * fluent mybatis version
     *
     * @return
     */
    public static String getVersionBanner() {
        Package pkg = MybatisUtil.class.getPackage();
        String version = (pkg != null ? pkg.getImplementationVersion() : "");
        StringBuilder buff = new StringBuilder()
            .append("  _____   _                          _    \n")
            .append(" |  ___| | |  _   _    ___   _ __   | |_  \n")
            .append(" | |_    | | | | | |  ( _ ) | '_ L  | __| \n")
            .append(" |  _|   | | | |_| | |  __) | | | | | |_  \n")
            .append(" |_|     |_| |___,_| |____| |_| |_| |___| \n")
            .append(" __  __         ____          _    _      \n")
            .append("| )  ( | _   _ | __ )   __ _ | |_ (_) ___ \n")
            .append("| |)(| || | | ||  _ L  { _` || __|| || __|\n")
            .append("| |  | || |_| || |_) || (_| || |_ | |(__ )\n")
            .append("|_|  |_| L__, ||____)  (__,_| L__||_||___}\n")
            .append("         |___)                            \n")
            .append(version == null ? "" : version + " \n");
        return buff.toString();
    }
}