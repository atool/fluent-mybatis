package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.annotation.NotField;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;

/**
 * MybatisUtil
 * 工具类
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unused"})
public class MybatisUtil {
    /**
     * 安全的进行字符串 format
     *
     * @param target 目标字符串
     * @param params format 参数
     * @return format 后的
     */
    public static String format(String target, Object... params) {
        if (target.contains("%s") && If.notEmpty(params)) {
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
        Escape_Char.put('\'', "\\'");
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
                if (!isStatic(field) &&
                    field.getAnnotation(NotField.class) == null &&
                    !map.containsKey(field.getName())) {
                    map.put(field.getName(), field);
                }
            }
            current = current.getSuperclass();
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 过滤 static 和 transient 关键字修饰的属性
     *
     * @param field Field
     * @return true: field is static modifier
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
     * @param property name of property
     * @param value    object
     */
    public static void assertNotNull(String property, Object value) {
        if (value == null) {
            throw FluentMybatisException.instance("the parameter[" + property + "] can't be null.", property);
        }
    }

    /**
     * 断言对象不能为null
     *
     * @param property name of property
     * @param value1   object
     * @param value2   object
     */
    public static <T> void assertNotNull(String property, T value1, T value2) {
        if (value1 == null || value2 == null) {
            throw FluentMybatisException.instance("the parameter[%s] can't be null.", property);
        }
    }

    /**
     * 断言字符串不能为空
     *
     * @param property name of property
     * @param value    string value
     */
    public static void assertNotBlank(String property, String value) {
        if (If.isBlank(value)) {
            throw FluentMybatisException.instance("the parameter[%s] can't be blank.", property);
        }
    }

    /**
     * 断言list参数不能为空
     *
     * @param property name of property
     * @param list     objects
     */
    public static void assertNotEmpty(String property, Collection list) {
        if (list == null || list.size() == 0) {
            throw FluentMybatisException.instance("the parameter[%s] can't be empty.", property);
        }
    }

    /**
     * 断言list参数不能为空
     *
     * @param property name of property
     * @param map      map
     */
    public static void assertNotEmpty(String property, Map map) {
        if (map == null || map.size() == 0) {
            throw FluentMybatisException.instance("the parameter[%s] can't be empty.", property);
        }
    }

    /**
     * 断言数组array参数不能为空
     *
     * @param property name of property
     * @param array    objects
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

    public static Integer[] toArray(int[] nums) {
        if (nums == null) {
            return null;
        } else {
            return Arrays.stream(nums).boxed().toArray(Integer[]::new);
        }
    }

    public static Long[] toArray(long[] nums) {
        if (nums == null) {
            return null;
        } else {
            return Arrays.stream(nums).boxed().toArray(Long[]::new);
        }
    }

    @SuppressWarnings("all")
    public static <O> boolean isCollection(O... args) {
        return args != null && args.length == 1 && args[0] instanceof Collection;
    }

    public static String trim(String str) {
        return If.isBlank(str) ? EMPTY : str.trim();
    }

    /**
     * 获取表名
     *
     * @param klass  Entity类名称
     * @param prefix 表名称前缀
     * @param suffix Entity类后缀
     * @return ignore
     */
    public static String tableName(String klass, String prefix, String suffix) {
        if (klass.endsWith(suffix)) {
            klass = klass.substring(0, klass.length() - suffix.length());
        }
        return prefix + camelToUnderline(klass, false);
    }

    /**
     * 下划线
     */
    public static final char UNDERLINE = '_';

    /**
     * 驼峰转下划线
     *
     * @param string  要转换的字符串
     * @param toUpper 统一转大写
     * @return ignore
     */
    public static String camelToUnderline(String string, boolean toUpper) {
        if (If.isBlank(string)) {
            return "";
        }
        int len = string.length();
        StringBuilder buff = new StringBuilder(len + 10);
        for (int pos = 0; pos < len; pos++) {
            char ch = string.charAt(pos);
            if (pos != 0 && Character.isUpperCase(ch)) {
                buff.append(UNDERLINE);
            }
            if (toUpper) {
                buff.append(Character.toUpperCase(ch));  //统一都转大写
            } else {
                buff.append(Character.toLowerCase(ch));  //统一都转小写
            }
        }
        return buff.toString();
    }

    /**
     * 下划线转驼峰命名
     *
     * @param input        text
     * @param firstCapital true: 首字母大写, false:首字母小写
     * @return ignore
     */
    public static String underlineToCamel(String input, boolean firstCapital) {
        if (If.isBlank(input)) {
            return "";
        }
        boolean first = true;
        boolean underline = false;
        StringBuilder buff = new StringBuilder(input.length());
        for (char ch : input.toCharArray()) {
            if (ch == '_') {
                if (!first) {
                    underline = true;
                }
                continue;
            }
            if (first) {
                /* 首字母 **/
                buff.append(firstCapital ? Character.toUpperCase(ch) : Character.toLowerCase(ch));
            } else if (underline) {
                /* 下划线转驼峰 **/
                buff.append(Character.toUpperCase(ch));
            } else {
                /* 默认将其他位置的字母转为小写 **/
                // buff.append(ch);
                buff.append(Character.toLowerCase(ch));
            }
            first = false;
            underline = false; 
        }
        return buff.toString();
    }

    /**
     * 实体首字母大写
     *
     * @param name 待转换的字符串
     * @param del  删除的前缀
     * @return 转换后的字符串
     */
    public static String capitalFirst(String name, String del) {
        if (!If.isBlank(name)) {
            if (del != null && name.startsWith(del)) {
                name = name.substring(del.length());
            }
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        } else {
            return "";
        }
    }

    /**
     * 实体首字母小写
     *
     * @param name 待转换的字符串
     * @param del  删除的前缀
     * @return 转换后的字符串
     */
    public static String lowerFirst(String name, String del) {
        if (!If.isBlank(name)) {
            if (del != null && name.startsWith(del)) {
                name = name.substring(del.length());
            }
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        } else {
            return "";
        }
    }

    /**
     * relation属性关联查询方法名
     *
     * @param method      属性(字段)名称
     * @param entityClass Entity类名
     * @return ignore
     */
    public static String methodNameOfEntity(String method, Class entityClass) {
        return method + "Of" + entityClass.getSimpleName();
    }

    /**
     * relation属性关联查询方法名
     *
     * @param method      属性(字段)名称
     * @param entityClass Entity类名
     * @return ignore
     */
    public static String methodNameOfEntity(String method, String entityClass) {
        return method + "Of" + entityClass;
    }

    /**
     * Entity Class不是@FluentMybatis注解类异常
     *
     * @param clazz class
     * @return ignore
     */
    public static RuntimeException notFluentMybatisException(Class clazz) {
        return new RuntimeException("the class[" + clazz.getName() + "] is not a @FluentMybatis Entity or it's sub class.");
    }


    /**
     * 判断是否是数据库表字段名称
     * 非全数字, 只包含数字+字母+下划线组成
     *
     * @param input column name
     * @return ignore
     */
    public static boolean isColumnName(String input) {
        if (isBlank(input)) {
            return false;
        }

        int len = input.length();
        if (input.charAt(0) == '`' && input.charAt(len - 1) == '`') {
            len--;
        } else if (!isLetter(input.charAt(0))) {
            return false;
        }
        for (int index = 1; index < len; index++) {
            char ch = input.charAt(index);
            if (!isLetterOrDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字母 a-z, A-Z, 或 '_'
     *
     * @param ch letter
     * @return ignore
     */
    public static boolean isLetter(char ch) {
        return ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_' || ch == '-';
    }

    /**
     * a-z, A-Z, 0-9, -, _, $
     *
     * @param ch char
     * @return ignore
     */
    public static boolean isLetterOrDigit(char ch) {
        return ch < 128 && letterAndDigit[ch] == 1;
    }

    private static final char[] letterAndDigit = new char[128];

    static {
        for (int i = 0; i < 128; i++) {
            letterAndDigit[i] = 0;
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            letterAndDigit[c] = 1;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            letterAndDigit[c] = 1;
        }
        for (char c = '0'; c <= '9'; c++) {
            letterAndDigit[c] = 1;
        }
        letterAndDigit['_'] = 1;
        letterAndDigit['-'] = 1;
        letterAndDigit['$'] = 1;
    }

    public static boolean isSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    /**
     * 数字
     *
     * @param ch letter
     * @return ignore
     */
    public static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * fluent mybatis version
     *
     * @return ignore
     */
    public static String getVersionBanner() {
        Package pkg = MybatisUtil.class.getPackage();
        String version = (pkg != null ? pkg.getImplementationVersion() : "");
        return "" +
            "  _____   _                          _    \n" +
            " |  ___| | |  _   _    ___   _ __   | |_  \n" +
            " | |_    | | | | | |  ( _ ) | '_ L  | __| \n" +
            " |  _|   | | | |_| | |  __) | | | | | |_  \n" +
            " |_|     |_| |___,_| |____| |_| |_| |___| \n" +
            " __  __         ____          _    _      \n" +
            "| )  ( | _   _ | __ )   __ _ | |_ (_) ___ \n" +
            "| |)(| || | | ||  _ L  { _` || __|| || __|\n" +
            "| |  | || |_| || |_) || (_| || |_ | |(__ )\n" +
            "|_|  |_| L__, ||____)  (__,_| L__||_||___}\n" +
            "         |___)                            \n" +
            (version == null ? "" : version + " \n");
    }
}