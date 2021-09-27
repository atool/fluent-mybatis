package cn.org.atool.fluent.mybatis.utility;

import cn.org.atool.fluent.mybatis.If;
import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.NotField;
import cn.org.atool.fluent.mybatis.base.IEntity;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import cn.org.atool.fluent.mybatis.exception.FluentMybatisException;
import cn.org.atool.fluent.mybatis.mapper.PrinterMapper;
import cn.org.atool.fluent.mybatis.spring.MapperFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.org.atool.fluent.mybatis.If.isBlank;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.EMPTY;
import static cn.org.atool.fluent.mybatis.mapper.StrConstant.SPACE;

/**
 * MybatisUtil
 * 工具类
 *
 * @author darui.wu
 */
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
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
     * @param entityClass 反射类
     */
    public static List<Field> getFieldList(Class entityClass) {
        Map<String, Field> map = new HashMap<>();
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (!isStatic(field) &&
                field.getAnnotation(NotField.class) == null &&
                !map.containsKey(field.getName())) {
                map.put(field.getName(), field);
            }
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

    public static RuntimeException notFluentMybatisException(String clazz) {
        return new RuntimeException("the class[" + clazz + "] is not a @FluentMybatis Entity or it's sub class.");
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

    public static boolean isTableName(String table) {
        if (isBlank(table)) {
            return false;
        }
        for (char c : table.toCharArray()) {
            if (c >= 128 || letterAndDigit[c] == 0) {
                return false;
            }
        }
        return true;
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
     * 解析别名列表
     *
     * @param column 字段
     * @return ignore
     */
    public static List<String> parseAlias(String column) {
        int pos = -1;
        List<String> list = new ArrayList<>();
        StringBuilder buff = new StringBuilder();
        for (char c : (column + SPACE).toCharArray()) {
            if (pos <= 0 && isSpace(c)) {
                pos = 0;
            } else if (pos == 0 && (c == 'a' || c == 'A')) {
                pos = 1;
            } else if (pos == 1 && (c == 's' || c == 'S')) {
                pos = 2;
            } else if ((pos == 2 || pos == 3) && isSpace(c)) {
                pos = 3;
            } else if (pos >= 3 && (isLetterOrDigit(c))) {
                pos = 4;
                buff.append(c);
            } else if (pos == 4 && (isSpace(c) || c == ',')) {
                list.add(buff.toString());
                buff = new StringBuilder();
                pos = -1;
            } else {
                pos = -1;
            }
        }
        return list;
    }

    public static String unwrap(String column) {
        int len = column.length();
        char begin = column.charAt(0);
        char last = column.charAt(len - 1);
        if (!isLetterOrDigit(begin) && !isLetterOrDigit(last)) {
            return column.substring(1, len - 1);
        } else {
            return column;
        }
    }

    /**
     * 获取target的属性的属性
     *
     * @param target Object
     * @param fields 级联属性
     * @param <T>    属性值
     * @return Object
     */
    public static <T> T value(Object target, Field... fields) {
        Object value = target;
        try {
            for (Field f : fields) {
                if (value == null) {
                    return null;
                }
                value = f.get(value);
            }
            return (T) value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field field(Class declared, String fieldName) {
        try {
            Field field = declared.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 按逗号分割字符串
     *
     * @param text 字符串
     * @return 分割好的子字符串列表
     */
    public static List<String> splitByComma(String text) {
        return splitBy(Collections.singletonList(','), text);
    }

    public static List<String> splitBySpace(String text) {
        return splitBy(Arrays.asList(' ', '\t', '\n', '\r'), text);
    }

    /**
     * 按分隔符分割字符串
     *
     * @param delimiters 分隔符
     * @param text       字符串
     * @return 分割好的子字符串列表
     */
    public static List<String> splitBy(Collection<Character> delimiters, String text) {
        List<String> list = new ArrayList<>();
        StringBuilder item = new StringBuilder();
        char quotation = 0;
        char slash = 0;
        for (char ch : text.toCharArray()) {
            if (ch == '\\') {
                item.append(ch);
                slash++;
                continue;
            }
            if (delimiters.contains(ch) && quotation == 0) {
                list.add(item.toString());
                item = new StringBuilder();
                slash = 0;
                continue;
            }
            item.append(ch);
            if (slash % 2 == 1) {
                slash = 0;
                continue;
            }
            if (ch == '\'' || ch == '"') {
                if (quotation == 0) {
                    quotation = ch;
                } else if (quotation == ch) {
                    quotation = 0;
                }
            }
            slash = 0;
        }
        list.add(item.toString());
        return list;
    }

    public static String joinWithSpace(Object... objects) {
        return Stream.of(objects).map(String::valueOf).map(String::trim)
            .filter(If::notBlank).collect(Collectors.joining(SPACE));
    }

    /**
     * 返回标注@FluentMybatis注解Entity类
     *
     * @param eClass 实例类
     * @return ignore
     */
    public static Class<? extends IEntity> entityClass(Class eClass) {
        Class aClass = eClass;
        while (aClass != Object.class && aClass != RichEntity.class) {
            if (aClass.getAnnotation(FluentMybatis.class) != null) {
                return aClass;
            }
            aClass = aClass.getSuperclass();
        }
        throw new RuntimeException("the class[" + eClass.getName() + "] is not a @FluentMybatis Entity.");
    }

    public static void isMapperFactoryInitialized() {
        if (MapperFactory.isInited() || PrinterMapper.isPrint()) {
            return;
        }
        throw new FluentMybatisException("Please add MapperFactory to spring container management: \n\n" +
            "    @Bean\n" +
            "    public MapperFactory mapperFactory() {\n" +
            "        return new MapperFactory();\n" +
            "    }");
    }

    /**
     * fluent mybatis version
     *
     * @return ignore
     */
    public static String getVersionBanner() {
        Package pkg = MybatisUtil.class.getPackage();
        String version = (pkg != null ? pkg.getImplementationVersion() : "");
        return "\n\n" +
            "      _____   _                          _       \n" +
            "     |  ___| | |  _   _    ___   _ __   | |_     \n" +
            "     | |_    | | | | | |  / _ \\ | '_ \\  | __|    \n" +
            "     |  _|   | | | |_| | |  __/ | | | | | |_     \n" +
            "  __ |_|     |_|  \\__,_|  \\___| |_|_|_|  \\__|    \n" +
            " |  \\/  |  _   _  | |__     __ _  | |_  (_)  ___ \n" +
            " | |\\/| | | | | | | '_ \\   / _` | | __| | | / __|\n" +
            " | |  | | | |_| | | |_) | | (_| | | |_  | | \\__ \\\n" +
            " |_|  |_|  \\__, | |_.__/   \\__,_|  \\__| |_| |___/\n" +
            "           |___/                                 " +
            (version == null ? "" : "\n" + version + " \n");
    }
}